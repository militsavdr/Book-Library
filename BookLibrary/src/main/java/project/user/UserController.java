package project.user;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import project.book.Book;
import project.book.BookHolder;
import project.book.BookRequest;
import project.book.BookService;
 

@Controller
public class UserController {
	@Autowired
	private UserService usrSVC;
	@Autowired
	private BookService bookSVC;
	@RequestMapping("/user/dashboard")
	public String loadUser(Model model) {
		User user = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		//System.out.println(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	    model.addAttribute("name", usrSVC.getProfileFromID(user.getId()).getFullName());
	    model.addAttribute("book", new Book());
		return "dashboard";
	}
	
	@RequestMapping("/user/profile")
	public String getUserProfile(Model model){
		User user = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Profile profile = usrSVC.getProfileFromID(user.getId());
		model.addAttribute(profile);
		return "profile";
	 }
	
    @RequestMapping(value = "/user/profileEdit", method = RequestMethod.POST)
    public String updateProfile(@ModelAttribute("profile") Profile profile,  Model model) {
        // Update the profile 
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usrSVC.updateProfileForUser(user, profile);
        model.addAttribute("success", "Profile updated successfully!");
        return "redirect:/user/profile"; // Redirect after POST to prevent duplicate submissions
    }
    @RequestMapping(value = "/user/registerBook", method = RequestMethod.POST)
    public String registerBook(@ModelAttribute("book") Book book, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookSVC.registerBook(user.getId(), book);
        model.addAttribute("success", "Book registered successfully!");
        return "redirect:/user/dashboard";
    }
    @RequestMapping("/user/myBooks")
    public String showUserBooks(Model model) {
        // Get the currently logged-in user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book[] userBooks = bookSVC.showUserBook(user.getId());
        model.addAttribute("userBooks", userBooks);
        return "myBooks";
    }
    @RequestMapping(value = "/user/removeBook", method = RequestMethod.POST)
    public String removeBook(@ModelAttribute("book") Book book) {
        // Get the currently logged-in user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Remove the book
        bookSVC.removeBook(user.getId(), book);

        // Redirect to the user's book list page
        return "redirect:/user/myBooks";
    }
    @RequestMapping("/user/searchExact")
    public String searchBookExact(@ModelAttribute("title") String title,@ModelAttribute("author") String author, Model model) {
        BookHolder[] books = bookSVC.searchExact(title,author);
        model.addAttribute("searchResults", books);
        return "searchResults";
    }

    @RequestMapping("/user/searchApproximate")
    public String searchBookApproximate(@ModelAttribute("title") String title,@ModelAttribute("author") String author, Model model) {
        BookHolder[] books = bookSVC.searchApprox(title,author);
        model.addAttribute("searchResults", books);
        return "searchResults";
    }
    @RequestMapping("/user/recommended")
    public String showRecommendations(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile profile = usrSVC.getProfileFromID(user.getId());
        BookHolder[] recommendedBooks = bookSVC.showBookFromFavor(profile.getFav_array(),profile.getFav_authorsArray());
        model.addAttribute("recommendedBooks", recommendedBooks);
        return "recommended";
    }
    
    @RequestMapping(value = "/user/makeRequest", method = RequestMethod.POST)
    public String makeRequest(@RequestParam("id") int id, 
            					@RequestParam("ISBN") String isbn, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookSVC.makeRequest(user.getId(), id, isbn);
        model.addAttribute("successMessage", "Request made successfully!");
        return "redirect:/user/dashboard";
    }
    
    private class request{
    	public String ProviderName;
    	public String Title;
    	public String ISBN;
    	public int Provider;
    	public boolean approved;
    }
    
    @RequestMapping("/user/requests")
    public String getUserRequests(Model model) {
        // Get the currently logged-in user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BookRequest[] userRequests = bookSVC.getSelfRequests(user.getId());
        request[] requests = new request[userRequests.length];
        for(int i = 0 ;i < userRequests.length;i++) {
        	requests[i] = new request();
        	requests[i].ISBN = userRequests[i].getISBN();
        	requests[i].Provider = userRequests[i].getProvider();
        	requests[i].approved = userRequests[i].isApproved();
        	requests[i].Title = bookSVC.showProviderBookName(userRequests[i].getProvider(), userRequests[i].getISBN());
        	requests[i].ProviderName = usrSVC.getProfileFromID(userRequests[i].getProvider()).getFullName();
        }
        model.addAttribute("userRequests", requests);
        return "requests"; 
    }
    @RequestMapping("/user/requestsFromMe")
    public String showRequestsFromMe(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BookRequest[] requestsFromMe = bookSVC.getOthersRequests(user.getId());
        request[] requests = new request[requestsFromMe.length];
        for(int i = 0 ;i < requestsFromMe.length;i++) {
        	requests[i] = new request();
        	requests[i].ISBN = requestsFromMe[i].getISBN();
        	requests[i].Provider = requestsFromMe[i].getRequester();
        	requests[i].approved = requestsFromMe[i].isApproved();
        	requests[i].Title = bookSVC.showProviderBookName(requestsFromMe[i].getProvider(), requestsFromMe[i].getISBN());
        	requests[i].ProviderName = usrSVC.getProfileFromID(requestsFromMe[i].getRequester()).getFullName();
        }
        model.addAttribute("requestsFromMe", requests);
        return "requestsFromMe";
    }
    @RequestMapping(value = "/user/viewProfile", method = RequestMethod.GET)
    public String viewOtherProfile(@ModelAttribute("id") int requesterId, Model model) {
        Profile profile = usrSVC.getProfileFromID(requesterId);
        model.addAttribute("profile", profile);
        return "viewProfile";
    }    
    @RequestMapping(value = "/user/approveRequest", method = RequestMethod.GET)
    public String approveRequest(@ModelAttribute("id") int requesterId, @ModelAttribute("isbn") String isbn) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookSVC.approveRequest(user.getId(), requesterId, isbn);
        return "redirect:/user/requestsFromMe";
    }
    @RequestMapping(value = "/user/removeRequest", method = RequestMethod.POST)
    public String removeRequest(@RequestParam("provider") int providerId, 
                                @RequestParam("isbn") String isbn,
                                Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookSVC.removeRequest(user.getId(), providerId, isbn);
        return "redirect:/user/requests";
    }

}



