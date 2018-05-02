package auth;

import javax.ejb.Remote;

@Remote
public interface AuthBeanRemote {
	public LibSession getSession();
	public void login(String username, String hash);
	public void logout(LibSession session);
}
