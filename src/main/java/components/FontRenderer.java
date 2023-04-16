package components;

import SbyteX.Component;
import util.Logger;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            Logger.log("Found Font Renderer", 0);
        }
    }

    @Override
    public void update(float delta) {

    }
}
