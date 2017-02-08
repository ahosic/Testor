package at.fhooe.mc.hosic.mobilelearningapp.helpers;

/**
 * A message that gets passed to all observers of an observable object on its property changes.
 */

public class ModelChangedMessage {
    private MessageType mType;
    private Object mArgs;

    public ModelChangedMessage(MessageType _type, Object _args) {
        mType = _type;
        mArgs = _args;
    }

    public MessageType getType() {
        return mType;
    }

    public void setType(MessageType _type) {
        mType = _type;
    }

    public Object getArgs() {
        return mArgs;
    }

    public void setArgs(Object _args) {
        mArgs = _args;
    }
}
