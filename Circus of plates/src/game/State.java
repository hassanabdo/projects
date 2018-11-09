package game;

import java.io.Serializable;

public interface State extends Serializable{
	public void doAction(FalllingShape context);
}
