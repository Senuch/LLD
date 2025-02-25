package org.lld.pubsubsystem;

public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
    public void notifyObservers(Object arg);
}
