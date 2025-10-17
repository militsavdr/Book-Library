package project.user;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

	
	public void saveUser(User user);
	public boolean isUserPresent(User user);
	public void updateProfileForUser(User User,Profile profile);
	public Profile getProfileFromID(int ID);
}
