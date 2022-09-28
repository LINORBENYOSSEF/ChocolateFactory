package chocolate.factory.gui.controls;

import chocolate.factory.util.ImageLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChocolateButton extends Button {

    public ChocolateButton(String text, ImageLoader imageLoader) {
        super(text);

        Image image = imageLoader.getImage("images/button.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        setGraphic(imageView);
    }
}
