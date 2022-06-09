package com.example.gsmnotes.gui;

import com.example.gsmnotes.devices.DeviceService;
import com.example.gsmnotes.Application;
import com.example.gsmnotes.devices.Device;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.data.repository.core.RepositoryInformation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DeviceController implements Initializable {

    public TextField deviceModelTextField;
    public TextArea deviceInfo;
    public DatePicker deviceDatePicker;
    public TextField deviceNameTextField;
    public Button btnSave;
    public Button btnDelete;
    public Button btnCreate;
    @FXML
    private ListView deviceListView;
    DeviceService deviceService = Application.deviceService;
    public static final ObservableList<Device> devicesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.deviceListView.setItems(devicesList);
        this.devicesList.addAll(this.deviceService.getAllDevices());

        this.deviceListView.setCellFactory(lv -> {
            ListCell<Device> cell = new ListCell<Device>() {
                @Override
                protected void updateItem(Device item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        return;
                    }
                    DeviceTile c = new DeviceTile();
                    c.setDevice(item);
                    setGraphic(c);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    try {
                        Device d = (Device) deviceListView.getSelectionModel().getSelectedItem();
                        this.deviceSelected(e, d);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.consume();
                }
            });
            return cell;
        });
    }



    // Здесь будем хранить идентификатор устройства, которое прямо сейчас редактирует пользователь
    Device selectedDevice = null;

    public void deviceSelected(MouseEvent mouseEvent, Device selectedItem) throws IOException {

        // TODO: реализуем логику сохранения данных об устройстве
        /*
            если устройство уже было открыто, то нужно сохранить изменения
        */
        if (selectedDevice != null) {
            // selectedDevice.setId( обращаемся к соответствующему элементу интерфейса .getText() );
            this.deviceService.updateDevice(selectedDevice);
        }


        // если мы сохранили все, что можем отрисовывать данные следующего устройства
        this.selectedDevice = selectedItem;
        deviceNameTextField.setText(selectedDevice.getName());
        deviceModelTextField.setText(selectedDevice.getModel());
        deviceInfo.setText(selectedDevice.getHistory());
        deviceDatePicker.setValue(selectedDevice.getDate());

        //TODO DADDF

        // обращаемся к соответствующему элементу интерфейса .setText() ...


        System.out.println(selectedItem.getName());

        /*
        TODO: !сделать интерфейс, чтобы можно было редактировать отдельные поля!
        одно большое поле не прокатит!

        у тебя много полей у Девайса, это структурированные данные, которые должна быть возможность
        просматривать и изменять отдельно


        TODO: !предлагаю также вместо вкладок использовать одно окно, в котором можно просматривать, так и редактировать поля
        для этого просто нужно сделать кнопку Редактировать-Сохранить, чтобы текстовые поля стали доступны (setDisabled)

         */
    }

    public void btnCreateClicked(ActionEvent actionEvent) {
        Device d = new Device();
        // TODO: !задать настройки по умолчанию
        // имя по умолчанию
        // задать дату, равную сегодняшнему дню
        //selectedDevice.setDate();
        //todo дата время
        Application.deviceService.addDevice(d);
        this.devicesList.add(d);
        deviceListView.refresh();
    }

    public void btnDeleteClicked(ActionEvent actionEvent) {
        // TODO: добавить метод для удаления устройства

        // есть метод repository.save() а есть ... repository.delete()
        // Application.deviceService.deleteDevice();
        deviceListView.refresh();
    }

    public void btnSaveClicked(ActionEvent actionEvent) {
        selectedDevice.setName(deviceNameTextField.getText());
        selectedDevice.setDate(deviceDatePicker.getValue());
        selectedDevice.setModel(deviceModelTextField.getText());
        selectedDevice.setHistory(selectedDevice.getHistory());
        // !todo GINGER
        Application.deviceService.updateDevice(selectedDevice);
        deviceListView.refresh();

    }
}

