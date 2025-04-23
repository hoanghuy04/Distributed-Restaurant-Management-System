package gui.custom;

import java.rmi.RemoteException;

/**
 *
 * @author RAVEN
 */
public interface TableActionEvent {
    public void onView(int row) throws RemoteException;
}
