package project.user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class User implements UserDetails {

    private int id;
    private String username;
    private String password;
    
    
    public User() {
    	this.username = null;
    	this.password = null;
    	id = 0;
    }
    
    public User(int id,String username , String password) {
    	setUsername(username);
    	setPassword(password);
    	this.id = id;
    }
    @Override
    public String getUsername() {return username;}
    @Override
    public String getPassword() {return password;}
    
    public void setUsername(String username) {if(username.length() <= 20)this.username = username;}
    public void setPassword(String password) {this.password = password;}
    
    public int getId() {return id;}
	@Override
	public boolean isAccountNonExpired() {return true;}
	@Override
	public boolean isAccountNonLocked() {return true;}
	@Override
	public boolean isCredentialsNonExpired() {return true;}
	@Override
	public boolean isEnabled() {return true;}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {return Collections.singletonList(new SimpleGrantedAuthority("USER"));}


}
