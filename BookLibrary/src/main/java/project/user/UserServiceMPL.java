package project.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceMPL implements UserService, UserDetailsService {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;	//kruptografisi 
	@Autowired
	private DataSource dataSource;	//syndedh me db
	@Override
	public void saveUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        try {
	        Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
	        stmt.executeUpdate("INSERT INTO users(username,password) VALUES(\""+user.getUsername()+"\",\""+encodedPassword+"\");");	//apothikeush xrhsth
	        ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE username = \""+user.getUsername()+"\";"); //anaktisi id
	        rs.next();
	        stmt.executeUpdate("INSERT INTO profile(id) VALUES("+rs.getInt(1)+");"); //vale to id sto profile
	        rs.close();
	        stmt.close();
	        con.close();
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	System.out.println("Failed to connect , check credentials or activate service");
        }
	}
	@Override
	public boolean isUserPresent(User user) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = \""+user.getUsername()+"\";");
            rs.next();
            boolean cond = rs.getInt(1) > 0; //ama uparxei sthn bash o xrhsths me to sugkekrimeno username
            rs.close();
            con.close();
            return cond;
		} 
		catch (SQLException e) { 
			e.printStackTrace();
			System.out.println("Failed to connect , check credentials or activate service");
			return false;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = \""+username+"\";"); 
	        if (rs.isBeforeFirst()) {
	        	rs.next();
	        	User user = new User(rs.getInt(1),rs.getString(2),rs.getString(3)); //epstrepse antikeimeno user (afou vrethike)
	            rs.close();
	            stmt.close();
	            con.close();
	            return user;
	        }
	        else {
	        	rs.close();
	        	stmt.close();
	        	con.close();
	        	throw new UsernameNotFoundException("User not found");
	        }
		}
        catch (SQLException e) { 
			e.printStackTrace();
			System.out.println("Failed to connect , check credentials or activate service");
			throw new UsernameNotFoundException("User not found");
		}
	}
	@Override
	public void updateProfileForUser(User user,Profile profile) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			StringBuilder query = new StringBuilder();
			if(profile.getFullName() != null) query.append("FullName = \""+profile.getFullName()+"\""+",");
			if(profile.getAddress() != null) query.append("address = \""+profile.getAddress()+"\""+",");
			if(profile.getPhone_number() != null) query.append("phone_number = \""+profile.getPhone_number()+"\""+",");
			query.append("age = "+profile.getAge()+" ,");
			query.append("fav_genres = \'"+profile.getFav_genres()+"\'"+",");
			query.append("fav_authors = \""+profile.getFav_authors()+"\"");
            stmt.executeUpdate("UPDATE profile SET "+query.toString()+"WHERE id = "+user.getId()+";");
            stmt.close();
            con.close();
        } 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public Profile getProfileFromID(int ID) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM profile WHERE id = "+ID+";");
            rs.next();
            Profile profile = new Profile(ID, rs.getInt(3), rs.getString(2), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7));
            rs.close();
            stmt.close();
            con.close();
            return profile;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
