package components;

import SbyteX.Component;
import util.Logger;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;

    @Override
    public void start() {
        Logger.log("Starting", 0);
    }

    @Override
    public void update(float delta) {
        if (!firstTime) {
            Logger.log("Updating", 0);
            firstTime = true;
        }
    }
}
