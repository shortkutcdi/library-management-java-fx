package library.assistant.utils;

/**
https://stackoverflow.com/questions/18669209/javafx-what-is-the-best-way-to-display-a-simple-message/52874220#52874220

Small improvement of ceklock answer
added: 
-can't dimiss by click
-auto hide
-change popup position to bottom of window
-you dont have to looking for stage in file, you can pass any control view 
(button, label, container or what do you want)

JUST CALL:
Toast.show("YEEEEEEEEEEE", anyControl);
*/

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

 public class Toast {

        private static int TOAST_TIMEOUT = 1400;

        private static Popup createPopup(final String message) {
            final Popup popup = new Popup();
            popup.setAutoFix(true);
            Label label = new Label(message);
            label.getStylesheets().add("/css/mainStyles.css");
            label.getStyleClass().add("popup");
            popup.getContent().add(label);
            return popup;
        }

        public static void show(final String message, final Control control) {
            Stage stage = (Stage) control.getScene().getWindow();
            final Popup popup = createPopup(message);
            popup.setOnShown(e -> {
                popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
                popup.setY(stage.getY() + stage.getHeight() / 1.2 - popup.getHeight() / 2);
            });
            popup.show(stage);

            new Timeline(new KeyFrame(
                    Duration.millis(TOAST_TIMEOUT),
                    ae -> popup.hide())).play();
        }

}
