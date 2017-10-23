package com.stala.grzegorz.controller;


import com.stala.grzegorz.model.*;
import com.stala.grzegorz.model.security.PasswordResetToken;
import com.stala.grzegorz.model.security.Role;
import com.stala.grzegorz.model.security.UserRole;
import com.stala.grzegorz.service.*;
import com.stala.grzegorz.service.impl.UserSecurityService;
import com.stala.grzegorz.utility.MailConstructor;
import com.stala.grzegorz.utility.SecurityUtility;
import com.stala.grzegorz.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/")
    public String index(Model model) {

        List<Product> productList = productService.findFirst3ByActiveEquals(true);

        if (productList.size() >= 3)
            model.addAttribute("productList", productList);
        else
            model.addAttribute("notEnoughProducts", true);

        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest request) {

        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        model.addAttribute("classActiveLogin", true);

        return "myAccount";
    }

    @RequestMapping("/hours")
    public String hours() {
        return "hours";
    }

    @RequestMapping("/faq")
    public String faq() {
        return "faq";
    }

    @RequestMapping("/productList")
    public String productList(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        model.addAttribute("activeAll", true);

        return "productList";
    }

    @RequestMapping("/productDetail")
    public String productDetail(
            @PathParam("id") Long id, Model model, Principal principal
    ) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Product product = productService.findOne(id);

        model.addAttribute("product", product);

        List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);

        model.addAttribute("sizeList", Constants.PRODUCT_SIZE_MAP.keySet());
        model.addAttribute("size", Constants.PRODUCT_SIZE_MAP.keySet().iterator().next());

        return "productDetail";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model
    ) {

        model.addAttribute("classActiveForgetPassword", true);

        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(newEmail);

        model.addAttribute("forgetPasswordEmailSent", "true");


        return "myAccount";
    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userShippingList", user.getUserShippingList());
        if (user.getOrderList().isEmpty())
            model.addAttribute("emptyOrderList", true);
        else
            model.addAttribute("orderList", user.getOrderList());

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("classActiveMyAccount", true);


        return "myProfile";
    }


    @RequestMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(
            Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userShippingList", user.getUserShippingList());
        if (user.getOrderList().isEmpty())
            model.addAttribute("emptyOrderList", true);
        else
            model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }


    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(
            Model model, Principal principal
    ) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        model.addAttribute("userShippingList", user.getUserShippingList());
        if (user.getOrderList().isEmpty())
            model.addAttribute("emptyOrderList", true);
        else
            model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }


    @RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
    public String addNewShippingAddressPost(
            @ModelAttribute("userShipping") UserShipping userShipping,
            Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        model.addAttribute("user", user);
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("classActiveShipping", true);
        if (user.getOrderList().isEmpty())
            model.addAttribute("emptyOrderList", true);
        else
            model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }


    @RequestMapping("/updateUserShipping")
    public String updateUserShipping(
            @ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if (user.getId() != userShipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);

            model.addAttribute("userShipping", userShipping);


            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);

            model.addAttribute("userShippingList", user.getUserShippingList());
            if (user.getOrderList().isEmpty())
                model.addAttribute("emptyOrderList", true);
            else
                model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
    public String setDefaultShippingAddress(
            @ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingId, user);

        model.addAttribute("user", user);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userShippingList", user.getUserShippingList());
        if (user.getOrderList().isEmpty())
            model.addAttribute("emptyOrderList", true);
        else
            model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }


    @RequestMapping("/removeUserShipping")
    public String removeUserShipping(
            @ModelAttribute("id") Long userShippingId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(userShippingId);

        if (user.getId() != userShipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);

            userShippingService.removeById(userShippingId);

            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveShipping", true);

            model.addAttribute("userShippingList", user.getUserShippingList());
            if (user.getOrderList().isEmpty())
                model.addAttribute("emptyOrderList", true);
            else
                model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,
            @ModelAttribute("username") String username,
            Model model
    ) throws Exception {
        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        if (userService.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);

            return "myAccount";
        }

        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);

            return "myAccount";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        Role role = new Role();
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(email);

        model.addAttribute("emailSent", "true");
        model.addAttribute("orderList", user.getOrderList());

        return "myAccount";
    }


    @RequestMapping("/newUser")
    public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }

        User user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);

        return "myProfile";
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public String updateUserInfo(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {
        User currentUser = userService.findById(user.getId());

        if (currentUser == null) {
            throw new Exception("User not found");
        }

		/*check email already exists*/
        if (userService.findByEmail(user.getEmail()) != null) {
            if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
                model.addAttribute("emailExists", true);
                return "myProfile";
            }
        }
		
		/*check username already exists*/
        if (userService.findByUsername(user.getUsername()) != null) {
            if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
                model.addAttribute("usernameExists", true);
                return "myProfile";
            }
        }

//		update password
        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);

                return "myProfile";
            }
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        userService.save(currentUser);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);

        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCards", true);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping("/orderDetail")
    public String orderDetail(
            @RequestParam("id") Long orderId,
            Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        Order order = orderService.findOne(orderId);

        if (order.getUser().getId() != user.getId()) {
            return "badRequestPage";
        } else {
            List<CartItem> cartItemList = cartItemService.findByOrder(order);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("user", user);
            model.addAttribute("order", order);

            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            UserShipping userShipping = new UserShipping();
            model.addAttribute("userShipping", userShipping);


            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveOrders", true);
            model.addAttribute("displayOrderDetail", true);

            return "myProfile";
        }
    }


    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

}
