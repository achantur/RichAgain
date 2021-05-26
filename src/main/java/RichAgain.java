import gearth.extensions.ExtensionForm;
import gearth.extensions.ExtensionInfo;
import gearth.extensions.extra.tools.PacketInfoSupport;
import gearth.protocol.HMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

@ExtensionInfo(
        Title = "RichAgain",
        Description = "Be rich again in Habbo :)",
        Version = "V.1",
        Author = "achantur"
)

public class RichAgain extends ExtensionForm {
    public PacketInfoSupport packetInfoSupport;

    public Label diamondsLabel;
    public Label creditsLabel;
    public Label ducketsLabel;
    public Label hcLabel;

    public Spinner<Integer> diamonds;
    public Spinner<Integer> credits;
    public Spinner<Integer> duckets;
    public Spinner<Integer> hc;

    public Button generate;
    public Button reset;

    int diamondValue;
    int ducketValue;

    public static void main(String[] args) {
        runExtensionForm(args, RichAgain.class);
    }

    @Override
    public ExtensionForm launchForm(Stage primaryStage) throws Exception {
        primaryStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RichAgain.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("RichAgain");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);

        return loader.getController();
    }

    @Override
    protected void initExtension() {
        packetInfoSupport = new PacketInfoSupport(this);

        generate.setOnAction(event -> {
            handleDiamonds();
            handleCredits();
            handleDuckets();
            handleHc();
        });

        reset.setOnAction(event -> {
            packetInfoSupport.sendToServer("ScrGetUserInfoMessageComposer", "habbo_club");
            packetInfoSupport.sendToServer("GetCreditsInfoComposer");
            packetInfoSupport.sendToClient("HabboActivityPointNotificationMessageEvent",
                    ducketValue, 0, 0);
            packetInfoSupport.sendToClient("HabboActivityPointNotificationMessageEvent",
                    diamondValue, 0, 5);
        });

        packetInfoSupport.intercept(HMessage.Direction.TOSERVER, "ScrGetUserInfoMessageComposer", hMessage ->
                hMessage.setBlocked(true)
                );
    }

    public void handleDiamonds() {
        diamonds.increment(0);
        packetInfoSupport.sendToClient("HabboActivityPointNotificationMessageEvent",
                diamonds.valueProperty().getValue(), 0, 5);
    }

    public void handleCredits() {
        credits.increment(0);
        String parseCredits = String.valueOf(credits.valueProperty().getValue());
        packetInfoSupport.sendToClient("CreditBalanceEvent", parseCredits);
    }

    public void handleDuckets() {
        duckets.increment(0);
        packetInfoSupport.sendToClient("HabboActivityPointNotificationMessageEvent",
                duckets.valueProperty().getValue(), 0, 0);
    }

    public void handleHc() {
        hc.increment(0);
        packetInfoSupport.sendToClient("ScrSendUserInfoEvent",
                "club_habbo", hc.valueProperty().getValue(), 0, 0, 0, true, true, 0, 0, 9999, 0);
    }

}