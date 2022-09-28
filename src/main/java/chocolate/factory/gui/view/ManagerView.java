package chocolate.factory.gui.view;

import chocolate.factory.gui.ViewController;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class ManagerView extends AbstractView {

    private enum SubView {
        EMPLOYEE_DATA {
            @Override
            View createView(ImageLoader imageLoader) {
                return new EmployeeDataSubView(imageLoader);
            }
        },
        WORK_LOGS {
            @Override
            View createView(ImageLoader imageLoader) {
                return new EmployeeWorkLogsSubView();
            }
        },
        CHOCOLATE_ORDERS {
            @Override
            View createView(ImageLoader imageLoader) {
                return new ChocolateOrdersSubView(imageLoader);
            }
        },
        INGREDIENT_ORDERS {
            @Override
            View createView(ImageLoader imageLoader) {
                return new IngredientOrdersSubView(imageLoader);
            }
        },
        CHOCOLATES {
            @Override
            View createView(ImageLoader imageLoader) {
                return new ChocolatesSubView(imageLoader);
            }
        },
        INGREDIENTS {
            @Override
            View createView(ImageLoader imageLoader) {
                return new IngredientsSubView(imageLoader);
            }
        },
        CLIENTS_DATA {
            @Override
            View createView(ImageLoader imageLoader) {
                return new ClientsDataSubView(imageLoader);
            }
        }
        ;

        abstract View createView(ImageLoader imageLoader);
    }

    public ManagerView(ViewController viewController, ImageLoader imageLoader) {
        FlowPane pane = new FlowPane();
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setAlignment(Pos.CENTER);

        for (SubView subView : SubView.values()) {
            View view = subView.createView(imageLoader);
            getChildViews().add(view);

            Button button = new ChocolateButton(view.getName(), imageLoader);
            button.setOnAction((e)-> {
                viewController.setView(view);
            });
            pane.getChildren().add(button);
        }

        setCenter(pane);
    }

    @Override
    public boolean onEnter() {
        return true;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "Manager Portal";
    }
}
