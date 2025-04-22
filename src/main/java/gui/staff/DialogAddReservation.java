/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui.staff;
//Duong Hoang Huy

import  bus.*;
import bus.impl.CustomerBUSImpl;
import bus.impl.EmployeeBUSImpl;
import bus.impl.FloorBUSImpl;
import bus.impl.OrderBUSImpl;
import bus.impl.TableBUSImpl;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.zxing.WriterException;
import dal.connectDB.ConnectDB;
import model.CustomerEntity;
import model.EmployeeEntity;
import model.FloorEntity;
import model.OrderEntity;
import model.TableEntity;
import gui.FormLoad;
import gui.custom.combo_suggestion.ComboBoxSuggestion;
import gui.main.LoginGUI;
import jakarta.mail.MessagingException;
import java.util.*;
import jakarta.persistence.EntityManager;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.enums.OrderStatusEnum;
import model.enums.OrderTypeEnum;
import model.enums.PaymentMethodEnum;
import model.enums.PaymentStatusEnum;
import model.enums.ReservationStatusEnum;
import raven.toast.Notifications;
import util.*;

/**
 *
 * @author Huy.
 */
public class DialogAddReservation extends javax.swing.JDialog {

    EntityManager em;
    private CustomerBUS customerBUS;
    private EmployeeBUS employeeBUS;
    private FloorBUS floorBus;
    private TableBUS tableBUS;
    private OrderBUS orderBUS;

    private TreeMap<String, List<OrderEntity>> mapOfAllReservations;
    private DefaultComboBoxModel defaultComboBoxModel1;
    private DefaultComboBoxModel defaultComboBoxModel2;

    private CustomerEntity customerEntity;
    private LocalDateTime reservationDateTime;

    private boolean checkNewReservation = true;

    private String[] reservationTimes = new String[]{
        "09:00", "09:15", "09:30", "09:45",
        "10:00", "10:15", "10:30", "10:45",
        "11:00", "11:15", "11:30", "11:45",
        "12:00", "12:15", "12:30", "12:45",
        "13:00", "13:15", "13:30", "13:45",
        "14:00", "14:15", "14:30", "14:45",
        "15:00", "15:15", "15:30", "15:45",
        "16:00", "16:15", "16:30", "16:45",
        "17:00", "17:15", "17:30", "17:45",
        "18:00", "18:15", "18:30", "18:45",
        "19:00", "19:15", "19:30", "19:45",
        "20:00", "20:15", "20:30", "20:45",
        "21:00", "21:30"
    };

    private List<String> availableTables;
    private List<String> floors;

    private String[] completionTimes = new String[]{
        "00:30",
        "01:00",
        "01:30",
        "02:00",
        "02:30",
        "03:00"
    };
    ;
    private String[] paymentMethods;

    private TabReservation tabReservation;

    private LocalDate oldDate;
    private OrderEntity orderEntity = null;
    private String preNumberOfCust;

    /**
     * Creates new form DialogAddReservation
     */
    public DialogAddReservation(TreeMap<String, List<OrderEntity>> mapOfAllReservations, TabReservation tabReservation) {
        super(new JFrame(), true);
        this.tabReservation = tabReservation;
        this.oldDate = LocalDate.now();

        // Initialize variables
        this.customerBUS = FormLoad.customerBUS;
        this.employeeBUS = FormLoad.employeeBUS;
        this.floorBus = FormLoad.floorBUS;
        this.tableBUS = FormLoad.tableBUS;
        this.orderBUS = FormLoad.orderBUS;

        this.mapOfAllReservations = mapOfAllReservations;
        availableTables = new ArrayList<>();
        // availableTables = tableBUS.getAllEntities().stream().map(t -> ("Bàn " + Integer.parseInt(t.getTableId().substring(1)))).collect(Collectors.toList());

        floors = floorBus .getAllEntities().stream().map(f -> f.getName()).collect(Collectors.toList());
        paymentMethods = Arrays.stream(PaymentMethodEnum.values())
                .map(PaymentMethodEnum::getPaymentMethod)
                .toArray(String[]::new);

        FlatLightLaf.setup();
        initComponents();
        setUI();
        setLocationRelativeTo(this);
        setResizable(false);
        loadCbbTime();

        this.txtDate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadCbbTime();
                loadTables();
                loadCombinedTables();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        loadTables();
        loadCombinedTables();
    }

    public DialogAddReservation(TreeMap<String, List<OrderEntity>> mapOfAllReservations, TabReservation tabReservation, OrderEntity preOrder) {
        super(new JFrame(), true);
        this.tabReservation = tabReservation;
        this.oldDate = LocalDate.now();
        this.orderEntity = preOrder;
        em = ConnectDB.getEntityManager();
        customerBUS  = new CustomerBUS (em);
        employeeBUS = new EmployeeBUSImpl(em);
        floorBusImpl = new FloorBUSImpl(em);
        tableBUSImpl = new TableBUSImpl(em);
        orderBUSImpl = new OrderBUSImpl(em);
        this.mapOfAllReservations = mapOfAllReservations;
        availableTables = new ArrayList<>();
        floors = floorBusImpl.getAllEntities().stream().map(f -> f.getName()).collect(Collectors.toList());

        preNumberOfCust = "0";
        paymentMethods = Arrays.stream(PaymentMethodEnum.values())
                .map(PaymentMethodEnum::getPaymentMethod)
                .toArray(String[]::new);
        FlatLightLaf.setup();
        initComponents();
        // Tạo border màu xanh với độ dày 4 pixel
        setBorder(new LineBorder(Color.BLUE, 4, true));
        setLocationRelativeTo(this);
        setResizable(false);
        loadCbbTime();

        this.txtDate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadCbbTime();
                loadTables();
                loadCombinedTables();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

        });

        loadTables();
        loadDataOfReservation();
        loadCombinedTables();
    }

    public void setBorder(Border border) {
        // Đặt border cho JDialog bằng cách bọc JPanel
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(border);
    }

    private void setUI() {
        txtPhoneNumber.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 0123456789");
        txtDeposit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 100000");
    }

    public String[] getReservationTimes() {
        return reservationTimes;
    }

    public void setReservationTimes(String[] reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    public List<String> getAvailableTables() {
        return availableTables;
    }

    public void setAvailableTables(List<String> availableTables) {
        this.availableTables = availableTables;
    }

    public String[] getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String[] paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String[] getCompletionTimes() {
        return completionTimes;
    }

    public void setCompletionTimes(String[] completionTimes) {
        this.completionTimes = completionTimes;
    }

    private void loadDataOfReservation() {
        if (this.orderEntity != null) {
            this.txtPhoneNumber.setText(this.orderEntity.getCustomer().getPhone());
            this.txtName.setText(this.orderEntity.getCustomer().getName());
            this.txtDate.setText(this.orderEntity.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()));
            this.cbbTime.setSelectedItem(this.orderEntity.getReservationTime().toLocalTime().format(DatetimeFormatterUtil.getTimeFormatter()));
            this.txtNumberOfCust.setText(this.orderEntity.getNumberOfCustomer() + "");

            setSelectedCompletionTime();

            this.cbbTable.setSelectedItem(this.orderEntity.getTable().getName());
            this.cbbFloor.setSelectedItem(this.orderEntity.getTable().getFloor().getName());
            this.txtDeposit.setText(this.orderEntity.getDeposit() + "");

            List<String> listOfCombinedTableName = this.orderEntity.getCombinedTables().stream().map(t -> tableBUSImpl.getEntityById(t.getTableId()).getName()).toList();
            listOfCombinedTableName.stream()
                    .forEach(tableName -> {
                        this.cbbCombinedTables.addItemObject(tableName);
                    });
//            this.cbbCombinedTables.setSelectedItems(listOfCombinedTableName);
        }
    }

    public void setSelectedCompletionTime() {
        Duration duration = Duration.between(this.orderEntity.getReservationTime().toLocalTime(), this.orderEntity.getExpectedCompletionTime().toLocalTime());

        // Chuyển đổi Duration thành số giờ và phút
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        // Chuyển đổi thành chuỗi theo định dạng HH:mm
        String timeString = String.format("%02d:%02d", hours, minutes);

        // Đặt giá trị đã chuyển đổi vào combo box
        this.cbbCompletionTime.setSelectedItem(timeString);
    }

    private void loadCbbTime() {
        LocalDate date = LocalDate.parse(txtDate.getText(), DatetimeFormatterUtil.getDateFormatter());
        if (LocalDate.now().equals(date)) {
            LocalTime timeNow = LocalTime.now();
            int index = 0;
            for (String s : reservationTimes) {
                if (!timeNow.isAfter(LocalTime.parse(s, DatetimeFormatterUtil.getTimeFormatter()))) {
                    index = Arrays.asList(reservationTimes).indexOf(s);
                    break;
                }
            }

            String[] reservationTimesCopy = new String[reservationTimes.length - index];
            System.arraycopy(reservationTimes, index, reservationTimesCopy, 0, reservationTimes.length - index);
            cbbTime.setDefaultModel(reservationTimesCopy);
        } else {
            cbbTime.setDefaultModel(reservationTimes);
        }
    }

    private void loadTables() {
        reservationDateTime = LocalDateTime.of(LocalDate.parse(txtDate.getText(), DatetimeFormatterUtil.getDateFormatter()), LocalTime.parse(cbbTime.getSelectedItem().toString(), DatetimeFormatterUtil.getTimeFormatter()));
        String floorId = "F" + String.format("%04d", cbbFloor.getSelectedIndex() + 1);

        availableTables = tableBUSImpl.getListOfAvailableTables(floorId, reservationDateTime, 0).stream().map(t -> t.getName()).collect(Collectors.toList());

        if (this.orderEntity != null) {
            defaultComboBoxModel1 = new DefaultComboBoxModel();
            defaultComboBoxModel1.addElement(this.orderEntity.getTable().getName());
            for (String tableName : availableTables) {
                defaultComboBoxModel1.addElement(tableName);
            }
        } else {
            defaultComboBoxModel1 = new DefaultComboBoxModel(availableTables.toArray(new String[0]));
        }
        cbbTable.setModel(defaultComboBoxModel1);
    }

    private void loadCombinedTables() {
        defaultComboBoxModel2 = new DefaultComboBoxModel();

        for (int i = 0; i < defaultComboBoxModel1.getSize(); i++) {
            defaultComboBoxModel2.addElement(defaultComboBoxModel1.getElementAt(i));
        }

        int selectedIndex = cbbTable.getSelectedIndex();
        if (selectedIndex >= 0) {
            defaultComboBoxModel2.removeElementAt(selectedIndex);
        }

        cbbCombinedTables.setModel(defaultComboBoxModel2);
    }

    private boolean createReservation() {
        if (this.customerEntity == null && txtName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin khách hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            LocalTime expectedCompletionTime = LocalTime.parse(cbbCompletionTime.getSelectedItem().toString(), DatetimeFormatterUtil.getTimeFormatter());
            LocalDateTime completionTime = reservationDateTime.plusHours(expectedCompletionTime.getHour())
                    .plusMinutes(expectedCompletionTime.getMinute());

            int numberOfCust = Integer.parseInt(txtNumberOfCust.getText());

            double deposit = txtDeposit.getText().trim().length() != 0 ? Double.parseDouble(txtDeposit.getText()) : 0;

            EmployeeEntity emp = LoginGUI.emp;

            String floorId = "F" + String.format("%04d", cbbFloor.getSelectedIndex() + 1);

            TableEntity table = tableBUSImpl.findByName(cbbTable.getSelectedItem().toString(), floorId);

            List<TableEntity> listOfCombinedTable = new ArrayList<>();

            List<String> strs = cbbCombinedTables.getSelectedItems();

            for (String tableName : strs) {
                TableEntity t = tableBUSImpl.findByName(tableName, floorId);
                listOfCombinedTable.add(t);
            }

            if (this.orderEntity == null) {
                this.orderEntity = new OrderEntity(reservationDateTime, completionTime, numberOfCust, deposit, customerEntity, emp, table, OrderStatusEnum.SINGLE,
                        OrderTypeEnum.ADVANCE, PaymentMethodEnum.convertToEnum(cbbPaymentMethod.getSelectedItem().toString()),
                        PaymentStatusEnum.UNPAID, ReservationStatusEnum.PENDING, new HashSet<>(), listOfCombinedTable);
                orderBUSImpl.insertEntity(orderEntity);
                tabReservation.getListOfAllReservations().add(orderEntity);
                this.tabReservation.addToMapOfAllReservations(orderEntity);
            } else {
                this.checkNewReservation = false;
                this.orderEntity.setReservationTime(reservationDateTime);
                this.orderEntity.setExpectedCompletionTime(completionTime);
                this.orderEntity.setNumberOfCustomer(numberOfCust);
                this.orderEntity.setDeposit(deposit);
                this.orderEntity.setEmployee(emp);
                this.orderEntity.setTable(table);

                List<TableEntity> tabless = orderEntity.getCombinedTables();
                if (tabless == null) {
                    tabless = new ArrayList<>();
                }

                for (TableEntity t : tabless) {
                    if (!listOfCombinedTable.contains(t)) {
                        this.tabReservation.getMainGUI().getOrderGUI().getCombinedTables().remove(t);
                    }
                }

                for (TableEntity t : listOfCombinedTable) {
                    if (!tabless.contains(t)) {
                        this.tabReservation.getMainGUI().getOrderGUI().getCombinedTables().add(t);
                    }
                }

                this.orderEntity.setCombinedTables(listOfCombinedTable);
                orderBUSImpl.updateEntity(orderEntity);
                this.tabReservation.addToMapOfAllReservations(orderEntity);
            }
            return true;
        }
    }

    private void sendEmailInBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Tạo mã QR cho đơn đặt chỗ
                QrCodeGenerationUtil.generateQrCode(orderEntity.getOrderId());
                try {
                    // Gửi email nếu là đơn đặt chỗ mới
                    MailSenderUtil.sendBookingConfirmationEmail(orderEntity);
                    Notifications.getInstance().show(
                            Notifications.Type.SUCCESS,
                            Notifications.Location.TOP_RIGHT,
                            10000,
                            "Thông báo: Gửi mail thông tin đơn đặt thành công cho khách hàng: " + orderEntity.getCustomer().getName()
                    );

                } catch (MessagingException ex) {
                    System.out.println("1");
                    ex.printStackTrace();
                } catch (IOException ex) {
                    System.out.println("2");
                    Logger.getLogger(DialogAddReservation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WriterException ex) {
                    System.out.println("3");
                    Logger.getLogger(DialogAddReservation.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }

            @Override
            protected void done() {
                // Xử lý sau khi hoàn thành tác vụ nếu cần
            }
        };

        // Bắt đầu thực thi công việc trong nền
        worker.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new gui.custom.datechooser.DateChooser(false);
        panelWrapper = new JPanel();
        panelHead = new JPanel();
        lblTitle = new javax.swing.JLabel();
        panelBody = new JPanel();
        lblReservationTime = new javax.swing.JLabel();
        txtDate = new gui.custom.RoundedTextField();
        lblTable = new javax.swing.JLabel();
        txtPhoneNumber = new gui.custom.RoundedTextField();
        lblPhoneNumber1 = new javax.swing.JLabel();
        cbbTable = new gui.custom.RoundedComboBox(availableTables);
        btnMinusNumber = new gui.custom.RoundedButton();
        btnPlusNumber = new gui.custom.RoundedButton();
        lblNumberOfCust = new javax.swing.JLabel();
        txtNumberOfCust = new gui.custom.RoundedTextField();
        lblCompletedTime = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new gui.custom.RoundedTextField();
        lblFloor = new javax.swing.JLabel();
        cbbFloor = new gui.custom.RoundedComboBox(floors);
        lblDeposit = new javax.swing.JLabel();
        lblCombinedTable = new javax.swing.JLabel();
        cbbPaymentMethod = new gui.custom.RoundedComboBox(paymentMethods);
        txtDeposit = new gui.custom.RoundedTextField();
        btnCancel = new gui.custom.RoundedButton();
        btnSave = new gui.custom.RoundedButton();
        cbbTime = new ComboBoxSuggestion();
        cbbCombinedTables = new gui.custom.ComboBoxMultiSelection();
        cbbCompletionTime = new gui.custom.RoundedComboBox(completionTimes);

        dateChooser1.setForeground(new Color(51, 153, 255));
        dateChooser1.setTextRefernce(txtDate);
        dateChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateChooser1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dateChooser1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dateChooser1MousePressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelWrapper.setBackground(new Color(255, 255, 255));

        panelHead.setBackground(new Color(204, 204, 204));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Thêm Đơn Đặt Bàn");
        lblTitle.setToolTipText("");

        javax.swing.GroupLayout panelHeadLayout = new javax.swing.GroupLayout(panelHead);
        panelHead.setLayout(panelHeadLayout);
        panelHeadLayout.setHorizontalGroup(
            panelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelHeadLayout.setVerticalGroup(
            panelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        lblReservationTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblReservationTime.setText("Thời gian nhận bàn");

        txtDate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDateMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtDateMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtDateMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtDateMouseReleased(evt);
            }
        });
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });
        txtDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtDateKeyReleased(evt);
            }
        });

        lblTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTable.setText("Chọn bàn");

        txtPhoneNumber.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPhoneNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhoneNumberFocusLost(evt);
            }
        });
        txtPhoneNumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtPhoneNumberMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtPhoneNumberMouseReleased(evt);
            }
        });
        txtPhoneNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneNumberActionPerformed(evt);
            }
        });
        txtPhoneNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                txtPhoneNumberKeyPressed(evt);
            }
            public void keyReleased(KeyEvent evt) {
                txtPhoneNumberKeyReleased(evt);
            }
        });

        lblPhoneNumber1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPhoneNumber1.setText("Số điện thoại");

        cbbTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbbTable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbTableItemStateChanged(evt);
            }
        });
        cbbTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbTableActionPerformed(evt);
            }
        });

        btnMinusNumber.setText("-");
        btnMinusNumber.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnMinusNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinusNumberActionPerformed(evt);
            }
        });

        btnPlusNumber.setText("+");
        btnPlusNumber.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPlusNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlusNumberActionPerformed(evt);
            }
        });

        lblNumberOfCust.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNumberOfCust.setText("Số lượng");

        txtNumberOfCust.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumberOfCust.setText("1");
        txtNumberOfCust.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNumberOfCust.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumberOfCustFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberOfCustFocusLost(evt);
            }
        });
        txtNumberOfCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumberOfCustActionPerformed(evt);
            }
        });

        lblCompletedTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCompletedTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCompletedTime.setText("Thời gian hoàn tất dự kiến");

        lblName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblName.setText("Tên khách hàng");

        txtName.setEnabled(false);
        txtName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        lblFloor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblFloor.setText("Chọn tầng");

        cbbFloor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbbFloor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbFloorItemStateChanged(evt);
            }
        });
        cbbFloor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbFloorActionPerformed(evt);
            }
        });

        lblDeposit.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDeposit.setText("Đặt tiền cọc");

        lblCombinedTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCombinedTable.setText("Chọn bàn gộp (nếu có)");

        cbbPaymentMethod.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtDeposit.setText("100000");
        txtDeposit.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDeposit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepositFocusLost(evt);
            }
        });
        txtDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositActionPerformed(evt);
            }
        });

        btnCancel.setText("Hủy");
        btnCancel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setText("Lưu đơn");
        btnSave.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        cbbTime.setModel(new DefaultComboBoxModel(reservationTimes));
        cbbTime.setMaximumRowCount(7);
        cbbTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbbTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbTimeItemStateChanged(evt);
            }
        });
        cbbTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbbTimeMouseClicked(evt);
            }
        });

        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(availableTables.toArray(new String[0]));
        cbbCombinedTables.setModel(defaultComboBoxModel);
        cbbCombinedTables.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbbCombinedTables.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbCombinedTablesItemStateChanged(evt);
            }
        });

        cbbCompletionTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblReservationTime, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(lblNumberOfCust, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(lblCompletedTime, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(btnMinusNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNumberOfCust, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnPlusNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbbCompletionTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPhoneNumber1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(lblCombinedTable, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(cbbPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDeposit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(cbbTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbbTime, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFloor, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cbbCombinedTables, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                                    .addComponent(cbbFloor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhoneNumber1)
                    .addComponent(lblName))
                .addGap(6, 6, 6)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReservationTime)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNumberOfCust)
                        .addComponent(lblCompletedTime)))
                .addGap(6, 6, 6)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMinusNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPlusNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumberOfCust, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbTime, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbCompletionTime, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTable)
                    .addComponent(lblFloor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbTable, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(cbbFloor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeposit)
                    .addComponent(lblCombinedTable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(cbbPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(txtDeposit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbbCombinedTables, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout panelWrapperLayout = new javax.swing.GroupLayout(panelWrapper);
        panelWrapper.setLayout(panelWrapperLayout);
        panelWrapperLayout.setHorizontalGroup(
            panelWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelWrapperLayout.setVerticalGroup(
            panelWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWrapperLayout.createSequentialGroup()
                .addComponent(panelHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelWrapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private void txtPhoneNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneNumberActionPerformed

    }//GEN-LAST:event_txtPhoneNumberActionPerformed

    private void btnMinusNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinusNumberActionPerformed
        int num = Integer.parseInt(txtNumberOfCust.getText());

        if (num > 1) {
            txtNumberOfCust.setText((num - 1) + "");
        } else {
            JOptionPane.showMessageDialog(null, "Số lượng khách phải lớn hơn 0!");
        }
    }//GEN-LAST:event_btnMinusNumberActionPerformed

    private void txtNumberOfCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumberOfCustActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberOfCustActionPerformed

    private void cbbTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbTableActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void cbbFloorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbFloorActionPerformed

    }//GEN-LAST:event_cbbFloorActionPerformed

    private void txtDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepositActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!checkPhoneNumber()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống hoặc độ dài phải từ 10 đến 15 ký số", "Warning", JOptionPane.WARNING_MESSAGE);
            txtPhoneNumber.requestFocus();
        } else if (!checkAvailableSeats()) {
            boolean check = false;
            List<TableEntity> list = tableBUSImpl.getListOfAvailableTables(null, reservationDateTime, 0);
            Map<FloorEntity, Integer> map = list.stream().collect(Collectors.groupingBy(TableEntity::getFloor, Collectors.summingInt(table -> 1)));

            for (Map.Entry<FloorEntity, Integer> entry : map.entrySet()) {
                Integer count = entry.getValue();
                if (count * 4 > Integer.parseInt(txtNumberOfCust.getText().trim())) {
                    check = true;
                }
            }
            if (check) {
                JOptionPane.showMessageDialog(null, "Số chổ đã đặt không phù hợp với số lượng khách", "Warning", JOptionPane.WARNING_MESSAGE);
                txtNumberOfCust.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Khung giờ bạn chọn không còn đủ số lượng chỗ. Vui lòng chọn khung giờ khác", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (txtNumberOfCust.getText().equalsIgnoreCase("0")) {
            JOptionPane.showMessageDialog(null, "Số lượng khách phải lớn hơn 0", "Warning", JOptionPane.WARNING_MESSAGE);
            txtNumberOfCust.requestFocus();
        } else if (!checkDeposit()) {
            JOptionPane.showMessageDialog(null, "Số tiền cọc phải gồm các kí số và lớn hơn hoặc bằng 0", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (createReservation()) {

//            List<TableEntity> list = this.orderEntity.getListOfCombinedTable()
//            JOptionPane.showMessageDialog(this, "Tạo đơn thành công");
                LocalDate date = LocalDate.parse(txtDate.getText(), DatetimeFormatterUtil.getDateFormatter());
                LocalTime time = LocalTime.parse(cbbTime.getSelectedItem().toString(), DatetimeFormatterUtil.getTimeFormatter());
//            tabReservation.updateNumberOfReservations(date, time, 1);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "Thao tác thành công!");
                this.dispose();
            }

            tabReservation.getMainGUI().loadMainGUI();

            if (checkNewReservation) {
                sendEmailInBackground();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtPhoneNumberKeyPressed(KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumberKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            this.customerEntity = customerBUS.findByPhone(txtPhoneNumber.getText().trim());
//            if (customerEntity == null) {
//                JOptionPane.showMessageDialog(null, "Khách hàng chưa tồn tại");
//            } else {
//                txtName.setText(customerEntity.getName());
//            }
//        }
    }//GEN-LAST:event_txtPhoneNumberKeyPressed

    private void btnPlusNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlusNumberActionPerformed
        int num = Integer.parseInt(txtNumberOfCust.getText());
        txtNumberOfCust.setText((num + 1) + "");
    }//GEN-LAST:event_btnPlusNumberActionPerformed

    private void cbbFloorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbFloorItemStateChanged
        cbbCombinedTables.clearSelectedItems();
        loadTables();
        loadCombinedTables();
    }//GEN-LAST:event_cbbFloorItemStateChanged

    private void cbbTableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbTableItemStateChanged
        loadCombinedTables();
        System.out.println(cbbTable.getSelectedItem().toString());
    }//GEN-LAST:event_cbbTableItemStateChanged

    private void txtDateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDateMouseExited
//        loadCbbTime();
//        loadTables();
//        loadCombinedTables();
    }//GEN-LAST:event_txtDateMouseExited

    private void cbbTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbTimeItemStateChanged
        loadTables();
        loadCombinedTables();
    }//GEN-LAST:event_cbbTimeItemStateChanged

    private void txtDateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDateMouseReleased

        loadCbbTime();
        loadTables();
        loadCombinedTables();
    }//GEN-LAST:event_txtDateMouseReleased


    private void txtDateMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDateMousePressed

    }//GEN-LAST:event_txtDateMousePressed

    private void dateChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateChooser1MouseClicked

    }//GEN-LAST:event_dateChooser1MouseClicked

    private void txtDateKeyReleased(KeyEvent evt) {//GEN-FIRST:event_txtDateKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtDateKeyReleased

    private void dateChooser1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateChooser1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_dateChooser1MouseExited

    private void dateChooser1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateChooser1MousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_dateChooser1MousePressed

    private void cbbTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbbTimeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbTimeMouseClicked

    private void txtDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDateMouseClicked
        this.oldDate = LocalDate.parse(txtDate.getText(), DatetimeFormatterUtil.getDateFormatter());
    }//GEN-LAST:event_txtDateMouseClicked

    private void cbbCombinedTablesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbCombinedTablesItemStateChanged

    }//GEN-LAST:event_cbbCombinedTablesItemStateChanged

    private void txtPhoneNumberMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPhoneNumberMouseExited
    }//GEN-LAST:event_txtPhoneNumberMouseExited

    private void txtPhoneNumberMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPhoneNumberMouseReleased

    }//GEN-LAST:event_txtPhoneNumberMouseReleased

    private void txtPhoneNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhoneNumberFocusLost
        if (!checkPhoneNumber()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống hoặc độ dài phải từ 10 đến 15 ký số", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            this.customerEntity = customerBUSImpl.findByPhone(txtPhoneNumber.getText().trim());
            if (customerEntity == null) {
                JOptionPane.showMessageDialog(null, "Khách hàng chưa tồn tại");
            } else {
                txtName.setText(customerEntity.getName());
            }
        }
    }//GEN-LAST:event_txtPhoneNumberFocusLost

    private void txtNumberOfCustFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberOfCustFocusGained
        preNumberOfCust = txtNumberOfCust.getText();
    }//GEN-LAST:event_txtNumberOfCustFocusGained

    private void txtNumberOfCustFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberOfCustFocusLost
        if (!txtNumberOfCust.getText().trim().matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null, "Số lượng khách hàng phải là số", "Warning", JOptionPane.WARNING_MESSAGE);
            txtNumberOfCust.setText(preNumberOfCust);
        }
    }//GEN-LAST:event_txtNumberOfCustFocusLost

    private void txtDepositFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepositFocusLost
//        if (!checkDeposit()) {
//            JOpcheckDeposittionPane.showMessageDialog(null, "Số tiền cọc phải gồm các kí số và lớn hơn hoặc bằng 0", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
    }//GEN-LAST:event_txtDepositFocusLost

    private void txtPhoneNumberKeyReleased(KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumberKeyReleased
               if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.customerEntity = customerBUSImpl.findByPhone(txtPhoneNumber.getText().trim());
            if (customerEntity == null) {
                JOptionPane.showMessageDialog(null, "Khách hàng chưa tồn tại");
            } else {
                txtName.setText(customerEntity.getName());
            }
        }
    }//GEN-LAST:event_txtPhoneNumberKeyReleased

    private boolean checkDeposit() {
        String deposit = txtDeposit.getText().trim();
        try {
            double depositValue = Double.parseDouble(deposit); // Chuyển sang kiểu số
            return depositValue >= 0; // Kiểm tra nếu lớn hơn 0
        } catch (NumberFormatException e) {
            return false; // Trường hợp không phải số
        }
    }

    private boolean checkPhoneNumber() {
        String phoneNumber = txtPhoneNumber.getText().trim();
        if (!phoneNumber.matches("^\\d{10,15}$")) {
            return false;
        }
        return true;
    }

    private boolean checkAvailableSeats() {
        List<TableEntity> listTable = new ArrayList<>();
        int totalSeats = 0;

        String floorId = "F" + String.format("%04d", cbbFloor.getSelectedIndex() + 1);
        totalSeats += tableBUSImpl.findByName(cbbTable.getSelectedItem().toString(), floorId).getCapacity();

        for (Object t : cbbCombinedTables.getSelectedItems()) {
            totalSeats += tableBUSImpl.findByName(t.toString(), floorId).getCapacity();
        }

        return Integer.parseInt(txtNumberOfCust.getText()) <= totalSeats;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.custom.RoundedButton btnCancel;
    private gui.custom.RoundedButton btnMinusNumber;
    private gui.custom.RoundedButton btnPlusNumber;
    private gui.custom.RoundedButton btnSave;
    private gui.custom.ComboBoxMultiSelection cbbCombinedTables;
    private gui.custom.RoundedComboBox cbbCompletionTime;
    private gui.custom.RoundedComboBox cbbFloor;
    private gui.custom.RoundedComboBox cbbPaymentMethod;
    private gui.custom.RoundedComboBox cbbTable;
    private gui.custom.RoundedComboBox cbbTime;
    private gui.custom.datechooser.DateChooser dateChooser1;
    private javax.swing.JLabel lblCombinedTable;
    private javax.swing.JLabel lblCompletedTime;
    private javax.swing.JLabel lblDeposit;
    private javax.swing.JLabel lblFloor;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNumberOfCust;
    private javax.swing.JLabel lblPhoneNumber1;
    private javax.swing.JLabel lblReservationTime;
    private javax.swing.JLabel lblTable;
    private javax.swing.JLabel lblTitle;
    private JPanel panelBody;
    private JPanel panelHead;
    private JPanel panelWrapper;
    private gui.custom.RoundedTextField txtDate;
    private gui.custom.RoundedTextField txtDeposit;
    private gui.custom.RoundedTextField txtName;
    private gui.custom.RoundedTextField txtNumberOfCust;
    private gui.custom.RoundedTextField txtPhoneNumber;
    // End of variables declaration//GEN-END:variables
}
