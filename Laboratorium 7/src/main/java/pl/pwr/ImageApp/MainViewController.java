package pl.pwr.ImageApp;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainViewController {

    private final Stage window;
    private final BorderPane mainPane = new BorderPane();

    private Button buttonLoad, buttonSave, buttonExecute;
    private Button buttonRotateLeft, buttonRotateRight;
    private ComboBox<String> choice;
    private ImageView originalImageView, changedImageView;

    private Image originalImage = null;
    private Image changedImage  = null;
    private File  currentFile   = null;
    private boolean isChanged   = false;

    private int rotationDegrees = 0;

    private static final String OP_NEGATIVE = "Negatyw";
    private static final String OP_THRESHOLD = "Progowanie";
    private static final String OP_COUNTURING = "Konturowanie";
    private static final String OP_GRAY = "Skala szarości";

    public MainViewController(Stage primaryStage) {
        this.window = primaryStage;
        createUI();
    }

    public BorderPane getMainPane() {
        return mainPane;
    }

    // UI
    private void createUI() {
        mainPane.setStyle("-fx-background-color: #ececec;");
        mainPane.setTop(buildHeader());
        mainPane.setLeft(buildPanel());
        mainPane.setCenter(buildImageGrid());
        mainPane.setBottom(buildFooter());
        setButtonsState(false);
    }

    //

    private HBox buildHeader() {
        HBox header = new HBox(12);
        header.setPadding(new Insets(10, 15, 10, 15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #003366; -fx-border-color: #002244; -fx-border-width: 0 0 2 0;");
        ImageView logo = new ImageView();
        Image logoImage = new Image(getClass().getResource("/PWr.png").toExternalForm());
        logo.setImage(logoImage);
        logo.setFitWidth(40);
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);
        Label titleLabel = new Label("ImageApp");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);
        Label subLabel = new Label("Wydział Informatyki i Telekomunikacji  |  Politechnika Wrocławska");
        subLabel.setFont(Font.font("Arial", 12));
        subLabel.setTextFill(Color.web("#aaccee"));
        VBox titles = new VBox(2, titleLabel, subLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label welcome = new Label("Witaj w edytorze graficznym!");
        welcome.setFont(Font.font("Arial", 12));
        welcome.setTextFill(Color.web("#ccddee"));
        header.getChildren().addAll(logo, titles, spacer, welcome);
        return header;
    }

    private VBox buildPanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(12, 10, 12, 10));
        panel.setPrefWidth(230);
        panel.setStyle("-fx-background-color: #dde3ea; -fx-border-color: #b0b8c4; -fx-border-width: 0 2 0 0;");
        buttonLoad = plainButton("Wczytaj obraz (.jpg)");
        buttonLoad.setOnAction(e -> handleLoadFile());
        Label opLabel = new Label("Operacja:");
        opLabel.setFont(Font.font("Arial", 12));
        choice = new ComboBox<>(FXCollections.observableArrayList(OP_NEGATIVE, OP_THRESHOLD, OP_COUNTURING, OP_GRAY));
        choice.setPromptText("Wybierz");
        choice.setMaxWidth(Double.MAX_VALUE);
        buttonExecute = plainButton("Wykonaj");
        buttonExecute.setOnAction(e -> executeOperation());
        Label rotLabel = new Label("Obrót:");
        rotLabel.setFont(Font.font("Arial", 12));
        buttonRotateLeft  = plainButton("Obróć w lewo (90°)");
        buttonRotateRight = plainButton("Obróć w prawo (90°)");
        buttonRotateLeft .setOnAction(e -> rotateImage(-90));
        buttonRotateRight.setOnAction(e -> rotateImage(90));
        Button buttonScale = plainButton("Skaluj obraz");
        buttonScale.setOnAction(e -> scaleDialog());
        buttonScale.setId("buttonScale");
        buttonSave = plainButton("Zapisz plik");
        buttonSave.setOnAction(e -> saveDialog());

        panel.getChildren().addAll(
                buttonLoad,
                new Separator(),
                opLabel, choice, buttonExecute,
                new Separator(),
                rotLabel, buttonRotateLeft, buttonRotateRight,
                new Separator(),
                buttonScale, buttonSave
        );
        return panel;
    }

    private GridPane buildImageGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #ececec;");
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(c1, c2);

        originalImageView = createImageView();
        changedImageView = createImageView();
        grid.add(createPreviewBox("Obraz oryginalny", originalImageView), 0, 0);
        grid.add(createPreviewBox("Obraz po modyfikacji", changedImageView), 1, 0);
        return grid;
    }

    private HBox buildFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(6, 12, 6, 12));
        footer.setStyle("-fx-background-color: #003366; -fx-border-color: #002244; -fx-border-width: 1 0 0 0;");
        Label authorInfo = new Label("Autor: Hubert Missar  |  Informatyczne Systemy Automatyki  |  280110");
        authorInfo.setFont(Font.font("Arial", 11));
        authorInfo.setTextFill(Color.web("#aaccee"));
        footer.getChildren().add(authorInfo);
        return footer;
    }

    // metody pomocnicze
    private Button plainButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Arial", 12));
        btn.setStyle("-fx-background-color: #c8d0da;" + "-fx-border-color: #8a96a4;" + "-fx-border-width: 1;" + "-fx-padding: 6 10;" + "-fx-background-radius: 0;" + "-fx-border-radius: 0;");
        return btn;
    }

    private ImageView createImageView() {
        ImageView iv = new ImageView();
        iv.setFitWidth(340);
        iv.setFitHeight(340);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }

    private VBox createPreviewBox(String title, ImageView iv) {
        VBox vbox = new VBox(6);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(8));
        vbox.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: #b0b8c4;" + "-fx-border-width: 1;");
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        StackPane container = new StackPane(iv);
        container.setPrefSize(350, 350);
        container.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 1;");
        vbox.getChildren().addAll(lbl, container);
        return vbox;
    }

    //  logika przycisków
    private void setButtonsState(boolean active) {
        buttonExecute.setDisable(!active);
        buttonSave.setDisable(!active);
        buttonRotateLeft.setDisable(!active);
        buttonRotateRight.setDisable(!active);
        choice.setDisable(!active);

        VBox panel = (VBox) mainPane.getLeft();
        if (panel != null) {
            panel.getChildren().stream().filter(n -> "buttonScale".equals(n.getId())).forEach(n -> n.setDisable(!active));
        }
    }

    //  wczytywanie pliku
    private void handleLoadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Wybierz plik obrazu JPG");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Obrazy JPG", "*.jpg", "*.jpeg"));
        File selected = fc.showOpenDialog(window);
        if (selected == null) return;
        String name = selected.getName().toLowerCase();
        if (!name.endsWith(".jpg") && !name.endsWith(".jpeg")) {
            Toast.show(window, "Niedozwolony format pliku", Toast.ToastType.ERROR);
            return;
        }
        try {
            clearCache();
            currentFile = selected;
            originalImage = new Image(selected.toURI().toString());
            if (originalImage.isError()) throw new Exception("Błąd ładowania obrazu");

            originalImageView.setImage(originalImage);
            setButtonsState(true);
            Toast.show(window, "Pomyślnie załadowano plik", Toast.ToastType.SUCCESS);
        } catch (Exception e) {
            Toast.show(window, "Nie udało się załadować pliku", Toast.ToastType.ERROR);
            setButtonsState(false);
        }
    }

    private void clearCache() {
        originalImage = null;
        changedImage = null;
        isChanged = false;
        rotationDegrees = 0;
        originalImageView.setImage(null);
        changedImageView.setImage(null);
        choice.setValue(null);
    }

    //  wykonywanie operacji
    private void executeOperation() {
        String op = choice.getValue();
        if (op == null) {
            Toast.show(window, "Nie wybrano operacji do wykonania", Toast.ToastType.WARNING);
            return;
        }
        if (originalImage == null) return;

        try {
            if (op.equals(OP_THRESHOLD)) {
                thresholdingDialog();
                return;
            }

            WritableImage result = null;

            if (op.equals(OP_NEGATIVE)) {
                result = negative(getSourceImage());
            }
            else if (op.equals(OP_GRAY)) {
                result = gray(getSourceImage());
            }
            else if (op.equals(OP_COUNTURING)) {
                result = contouring(getSourceImage());
            }

            if (result != null) {
                changedImage = result;
                changedImageView.setImage(changedImage);
                isChanged = true;
                toastSuccess(op);
            }
        } catch (Exception e) {
            toastError(op);
        }
    }

    private Image getSourceImage() {
        return (changedImage != null) ? changedImage : originalImage;
    }

    //  operacje przetwarzania
    private WritableImage negative(Image src) {
        int w = (int) src.getWidth(), h = (int) src.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = src.getPixelReader();
        PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                pw.setColor(x, y, new Color(1 - c.getRed(), 1 - c.getGreen(), 1 - c.getBlue(), c.getOpacity()));
            }
        return out;
    }

    private WritableImage gray(Image src) {
        int w = (int) src.getWidth(), h = (int) src.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = src.getPixelReader();
        PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                // wzor na wyliczanie luminancji
                double g = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
                pw.setColor(x, y, new Color(g, g, g, c.getOpacity()));
            }
        return out;
    }

    private WritableImage thresholding(Image src, int threshold) {
        double t = threshold / 255.0;
        int w = (int) src.getWidth(), h = (int) src.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = src.getPixelReader();
        PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                double g = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
                pw.setColor(x, y, g >= t ? Color.WHITE : Color.BLACK);
            }
        return out;
    }

    private WritableImage contouring(Image src) {
        int w = (int) src.getWidth(), h = (int) src.getHeight();
        WritableImage gray = gray(src);
        PixelReader pr = gray.getPixelReader();
        WritableImage out = new WritableImage(w, h);
        PixelWriter pw = out.getPixelWriter();
        // operator Sobela
        int[][] Gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] Gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                double gx = 0, gy = 0;
                for (int ky = -1; ky <= 1; ky++)
                    for (int kx = -1; kx <= 1; kx++) {
                        int nx = Math.min(Math.max(x + kx, 0), w - 1);
                        int ny = Math.min(Math.max(y + ky, 0), h - 1);
                        double v = pr.getColor(nx, ny).getRed();
                        gx += v * Gx[ky + 1][kx + 1];
                        gy += v * Gy[ky + 1][kx + 1];
                    }
                double mag = Math.min(Math.sqrt(gx * gx + gy * gy), 1.0);
                pw.setColor(x, y, new Color(mag, mag, mag, 1.0));
            }
        return out;
    }

    private WritableImage rotation(Image src, int degrees) {
        int w = (int) src.getWidth(), h = (int) src.getHeight();
        PixelReader pr = src.getPixelReader();
        boolean swap = (degrees == 90 || degrees == 270);
        int nw = swap ? h : w, nh = swap ? w : h;
        WritableImage out = new WritableImage(nw, nh);
        PixelWriter pw = out.getPixelWriter();
        int norm = ((degrees % 360) + 360) % 360;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                int nx, ny;
                switch (norm) {
                    case 90 -> { nx = (h - 1 - y); ny = x; }
                    case 180 -> { nx = (w - 1 - x); ny = (h - 1 - y); }
                    case 270 -> { nx = y; ny = (w - 1 - x); }
                    default -> { nx = x; ny = y; }
                }
                pw.setColor(nx, ny, c);
            }
        return out;
    }

    private WritableImage scaling(Image src, int targetW, int targetH) {
        WritableImage out = new WritableImage(targetW, targetH);
        PixelReader pr = src.getPixelReader();
        PixelWriter pw = out.getPixelWriter();
        double sx = src.getWidth() / targetW, sy = src.getHeight() / targetH;
        for (int y = 0; y < targetH; y++)
            for (int x = 0; x < targetW; x++) {
                int ox = Math.min((int)(x * sx), (int) src.getWidth() - 1);
                int oy = Math.min((int)(y * sy), (int) src.getHeight() - 1);
                pw.setColor(x, y, pr.getColor(ox, oy));
            }
        return out;
    }


    private void rotateImage(int delta) {
        if (originalImage == null) return;
        try {
            int norm = ((delta % 360) + 360) % 360;
            WritableImage rotated = rotation(getSourceImage(), norm);
            changedImage = rotated;
            changedImageView.setImage(changedImage);
            isChanged = true;
            Toast.show(window, "Obrót o 90° " + (delta > 0 ? "w prawo" : "w lewo") + " wykonany pomyślnie", Toast.ToastType.SUCCESS);
        } catch (Exception e) {
            Toast.show(window, "Nie udało się obrócić obrazu", Toast.ToastType.ERROR);
        }
    }

    private void thresholdingDialog() {
        Stage modal = createModal("Progowanie obrazu");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label info = new Label("Podaj wartość progu (0–255):");
        info.setFont(Font.font("Arial", 12));

        Spinner<Integer> spinner = new Spinner<>(0, 255, 128);
        spinner.setEditable(true);
        spinner.setMaxWidth(Double.MAX_VALUE);

        addIntegerFilter(spinner.getEditor(), 255);

        spinner.getValueFactory().setConverter(new IntegerStringConverter() {
            @Override
            public Integer fromString(String value) {
                if (value == null || value.trim().isEmpty()) {
                    return 0;
                }
                try {
                    return super.fromString(value);
                } catch (NumberFormatException e) {
                    return spinner.getValue();
                }
            }
        });

        HBox btnBox = new HBox(8);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        Button btnOk = plainButton("Wykonaj progowanie");
        Button btnCancel = plainButton("Anuluj");
        btnBox.getChildren().addAll(btnCancel, btnOk);

        layout.getChildren().addAll(info, spinner, btnBox);
        modal.setScene(new Scene(layout, 320, 130));

        btnCancel.setOnAction(e -> modal.close());
        btnOk.setOnAction(e -> {
            try {
                WritableImage result = thresholding(getSourceImage(), spinner.getValue());
                changedImage = result;
                changedImageView.setImage(changedImage);
                isChanged = true;
                modal.close();
                Toast.show(window, "Progowanie zostało przeprowadzone pomyślnie!", Toast.ToastType.SUCCESS);
            } catch (Exception ex) {
                Toast.show(window, "Nie udało się wykonać progowania.", Toast.ToastType.ERROR);
            }
        });
        modal.showAndWait();
    }

    private void scaleDialog() {
        if (originalImage == null) return;

        Stage modal = createModal("Skaluj obraz");

        VBox layout = new VBox(8);
        layout.setPadding(new Insets(15));

        Label infoW = new Label("Szerokość (1–3000 px):");
        infoW.setFont(Font.font("Arial", 12));
        TextField fieldW = new TextField(String.valueOf((int) getSourceImage().getWidth()));
        Label errW = new Label();
        errW.setTextFill(Color.RED);
        errW.setFont(Font.font("Arial", 11));
        Label infoH = new Label("Wysokość (1–3000 px):");
        infoH.setFont(Font.font("Arial", 12));
        TextField fieldH = new TextField(String.valueOf((int) getSourceImage().getHeight()));
        Label errH = new Label();
        errH.setTextFill(Color.RED);
        errH.setFont(Font.font("Arial", 11));
        addIntegerFilter(fieldW, 3000);
        addIntegerFilter(fieldH, 3000);

        Button btnRestore = plainButton("Przywroc oryginalne wymiary");
        btnRestore.setOnAction(e -> {
            fieldW.setText(String.valueOf((int) originalImage.getWidth()));
            fieldH.setText(String.valueOf((int) originalImage.getHeight()));
        });

        HBox btnBox = new HBox(8);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        Button btnOk     = plainButton("Zmien rozmiar");
        Button btnCancel = plainButton("Anuluj");
        btnBox.getChildren().addAll(btnCancel, btnOk);
        layout.getChildren().addAll(infoW, fieldW, errW, infoH, fieldH, errH, btnRestore, btnBox);
        modal.setScene(new Scene(layout, 320, 290));
        btnCancel.setOnAction(e -> modal.close());
        btnOk.setOnAction(e -> {
            boolean valid = true;
            if (fieldW.getText().trim().isEmpty()) { errW.setText("Pole jest wymagane"); valid = false; }
            else errW.setText("");
            if (fieldH.getText().trim().isEmpty()) { errH.setText("Pole jest wymagane"); valid = false; }
            else errH.setText("");
            if (!valid) return;
            try {
                int tw = Integer.parseInt(fieldW.getText().trim());
                int th = Integer.parseInt(fieldH.getText().trim());
                if (tw <= 0 || th <= 0 || tw > 3000 || th > 3000) {
                    Toast.show(window, "Wymiary muszą być w zakresie 1–3000", Toast.ToastType.ERROR);
                    return;
                }
                changedImage = scaling(getSourceImage(), tw, th);
                changedImageView.setImage(changedImage);
                isChanged = true;
                modal.close();
                Toast.show(window, "Skalowanie wykonano pomyślnie (" + tw + "x" + th + ")", Toast.ToastType.SUCCESS);
            } catch (NumberFormatException ex) {
                Toast.show(window, "Nieprawidłowe wartości wymiarów", Toast.ToastType.ERROR);
            }
        });
        modal.showAndWait();
    }

    // zapisywanie pliku
    private void saveDialog() {
        Stage modal = createModal("Zapisz plik jako");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        if (!isChanged) {
            Label warn = new Label("Na pliku nie zostaly wykonane zadne operacje!");
            warn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            warn.setTextFill(Color.web("#7a3a00"));
            warn.setStyle("-fx-background-color: #ffe0a0; -fx-padding: 6; -fx-border-color: #cc8800; -fx-border-width: 1;");
            warn.setMaxWidth(Double.MAX_VALUE);
            layout.getChildren().add(warn);
        }

        Label prompt = new Label("Podaj nazwę pliku (3–100 znaków):");
        prompt.setFont(Font.font("Arial", 12));
        TextField nameField = new TextField();
        nameField.textProperty().addListener((obs, old, nv) -> {
            if (nv.length() > 100) nameField.setText(old);
        });

        Label errLabel = new Label();
        errLabel.setTextFill(Color.RED);
        errLabel.setFont(Font.font("Arial", 11));

        HBox btnBox = new HBox(8);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        Button btnSave   = plainButton("Zapisz");
        Button btnCancel = plainButton("Anuluj");
        btnBox.getChildren().addAll(btnCancel, btnSave);

        layout.getChildren().addAll(prompt, nameField, errLabel, btnBox);
        modal.setScene(new Scene(layout, 380, isChanged ? 150 : 210));

        btnCancel.setOnAction(e -> { nameField.clear(); modal.close(); });
        btnSave.setOnAction(e -> {
            String fileName = nameField.getText().trim();
            if (fileName.length() < 3) { errLabel.setText("Wpisz co najmniej 3 znaki"); return; }
            modal.close();
            saveFile(fileName);
        });
        modal.showAndWait();
    }

    private void saveFile(String targetName) {
        String fullName = targetName + ".jpg";

        File picturesDir = new File(System.getProperty("user.home"), "Pictures");
        if (!picturesDir.exists()) picturesDir = new File(System.getProperty("user.home"), "Obrazy");
        if (!picturesDir.exists()) picturesDir = new File(System.getProperty("user.home"));

        File dest = new File(picturesDir, fullName);
        if (dest.exists()) {
            Toast.show(window, "Plik " + fullName + " juz istnieje w systemie. Podaj inna nazwe pliku!", Toast.ToastType.ERROR);
            return;
        }

        Image imageToSave = (changedImage != null) ? changedImage : originalImage;
        try {
            BufferedImage buffered = javafx.embed.swing.SwingFXUtils.fromFXImage(imageToSave, null);
            BufferedImage rgb = new BufferedImage(buffered.getWidth(), buffered.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgb.createGraphics().drawImage(buffered, 0, 0, java.awt.Color.WHITE, null);
            boolean ok = ImageIO.write(rgb, "jpg", dest);
            if (!ok) throw new IOException("ImageIO.write zwrocilo false");
            Toast.show(window, "Zapisano obraz w pliku " + fullName, Toast.ToastType.SUCCESS);
        } catch (IOException e) {
            Toast.show(window, "Nie udalo sie zapisac pliku " + fullName, Toast.ToastType.ERROR);
        }
    }

    //  toasty
    private void toastSuccess(String op) {
        String msg = switch (op) {
            case OP_NEGATIVE -> "Negatyw został wygenerowany pomyślnie!";
            case OP_COUNTURING -> "Konturowanie zostało przeprowadzone pomyślnie!";
            case OP_GRAY -> "Skala szarości zastosowana pomyślnie!";
            default -> "GOTOWE!";
        };
        Toast.show(window, msg, Toast.ToastType.SUCCESS);
    }

    private void toastError(String op) {
        String msg = switch (op) {
            case OP_NEGATIVE -> "Nie udało się wykonać negatywu.";
            case OP_COUNTURING -> "Nie udało się wykonać konturowania.";
            case OP_GRAY -> "Nie udało się zastosować skali szarości.";
            default -> "Nie udało się wykonać operacji.";
        };
        Toast.show(window, msg, Toast.ToastType.ERROR);
    }

    // okno modalne
    private Stage createModal(String title) {
        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(window);
        modal.setTitle(title);
        modal.setResizable(false);
        return modal;
    }

    // metoda filtrująca czy użytkownik wpisuje liczby całkowite
    private void addIntegerFilter(TextField field, int max) {
        field.textProperty().addListener((obs, old, nv) -> {
            if (!nv.matches("\\d*")) {
                field.setText(nv.replaceAll("[^\\d]", ""));
            } else if (!nv.isEmpty()) {
                try {
                    if (Integer.parseInt(nv) > max) field.setText(old);
                } catch (NumberFormatException e) {
                    field.setText(old);
                }
            }
        });
    }
}