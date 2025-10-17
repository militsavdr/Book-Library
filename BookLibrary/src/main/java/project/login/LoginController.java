package project.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import project.user.User;
import project.user.UserService;


@Controller
public class LoginController {
	
	@Autowired
	UserService userService;
	
    @RequestMapping("/login")
    public String login(Model model){
        return "signin";
    }
	
	
    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());	//exei prosvasi sto antikeimeno
        return "signup";
    }
    
    @RequestMapping("/save")
    public String registerUser(@ModelAttribute("user") User user , Model model) { //xrhsimopoei to model gia ta dedomena pou ypoballontai apo forma
    	if(user.getUsername() != null) {
	    	if(userService.isUserPresent(user)) {
	    		model.addAttribute("successMessage","User already registered");
	    		return "signup";
	    	}
	    	else{
	    		userService.saveUser(user);
	    		model.addAttribute("successMessage", "User registered successfully!");
	    		return "signin";
	    	}
    	}
    	else {
    		model.addAttribute("successMessage", "Username too long, over 20 characters");
    		return "signup";
    	}
    	
    }
	
	
}
