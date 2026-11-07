import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class HotelDashboard extends JFrame {

    // ================= COLOUR PALETTE =================
    static final Color NAVY      = new Color(15, 48, 85);
    static final Color NAVY_DARK = new Color(10, 25, 47);
    static final Color SIDEBAR   = new Color(20, 33, 61);
    static final Color HOVER     = new Color(42, 64, 108);
    static final Color ORANGE    = new Color(255, 159, 28);
    static final Color BG        = new Color(238, 243, 250);

    static final String FONT = "Segoe UI";

    SideButton activeBtn;

    public HotelDashboard() {

        setTitle("Smart Hotel Reservation System");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // ================= HEADER (gradient) =================
        GradientPanel header = new GradientPanel(NAVY_DARK, new Color(0, 119, 182), 0);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(1200, 80));

        JLabel title = new JLabel("   SMART HOTEL RESERVATION SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(FONT, Font.BOLD, 25));

        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 22));
        rightBox.setOpaque(false);

        JLabel admin = new JLabel("Admin");
        admin.setForeground(Color.WHITE);
        admin.setFont(new Font(FONT, Font.BOLD, 15));

        RoundButton logout = new RoundButton("Logout", new Color(231, 76, 60));
        logout.setPreferredSize(new Dimension(95, 34));
        logout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Do you want to logout?",
                    "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) System.exit(0);
        });

        rightBox.add(admin);
        rightBox.add(logout);

        header.add(title, BorderLayout.WEST);
        header.add(rightBox, BorderLayout.EAST);

        // ================= SIDEBAR =================
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(220, 665));
        side.setBackground(SIDEBAR);

        JPanel menuPanel = new JPanel(new GridLayout(8, 1, 0, 4));
        menuPanel.setBackground(SIDEBAR);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        String[] menu = {
                "Dashboard", "Manage Rooms", "Bookings", "Customers",
                "Payments", "Smart Room Search", "Reports", "Settings"
        };

        for (String item : menu) {
            SideButton button = new SideButton(item);
            menuPanel.add(button);

            if (item.equals("Dashboard")) {
                button.active = true;
                activeBtn = button;
            }

            button.addActionListener(e -> {
                activeBtn.active = false;
                activeBtn.repaint();
                button.active = true;
                activeBtn = button;
                button.repaint();
            });

            if (item.equals("Smart Room Search")) {
                button.addActionListener(e -> smartSearch());
            }
        }
        side.add(menuPanel, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);

        JLabel heading = new JLabel("Dashboard Overview");
        heading.setFont(new Font(FONT, Font.BOLD, 28));
        heading.setForeground(NAVY);
        heading.setBorder(BorderFactory.createEmptyBorder(22, 25, 10, 10));
        center.add(heading, BorderLayout.NORTH);

        // ================= CONTENT =================
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));

        // ---- Colourful statistic cards ----
        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 110));

        cards.add(card("TOTAL ROOMS", "50",
                new Color(52, 152, 219), new Color(41, 98, 255)));
        cards.add(card("AVAILABLE ROOMS", "32",
                new Color(46, 204, 113), new Color(22, 160, 133)));
        cards.add(card("BOOKED ROOMS", "18",
                new Color(255, 159, 28), new Color(231, 76, 60)));
        cards.add(card("TOTAL RESERVATIONS", "25",
                new Color(155, 89, 182), new Color(106, 17, 203)));

        content.add(cards, BorderLayout.NORTH);

        // ---- Quick actions ----
        JPanel actions = new JPanel(new GridLayout(2, 3, 15, 15));
        actions.setOpaque(false);

        RoundButton addRoom   = action("Add Room",          new Color(41, 128, 185));
        RoundButton viewRooms = action("View Rooms",        new Color(39, 174, 96));
        RoundButton bookings  = action("View Bookings",     new Color(230, 126, 34));
        RoundButton customers = action("Customer Details",  new Color(142, 68, 173));
        RoundButton bill      = action("Generate Bill",     new Color(192, 57, 43));
        RoundButton smart     = action("Find Suitable Room", new Color(22, 160, 133));

        actions.add(addRoom);
        actions.add(viewRooms);
        actions.add(bookings);
        actions.add(customers);
        actions.add(bill);
        actions.add(smart);

        content.add(section("Quick Actions", actions), BorderLayout.CENTER);

        // ---- Bottom section ----
        JPanel bottom = new JPanel(new GridLayout(1, 2, 15, 15));
        bottom.setOpaque(false);
        bottom.setPreferredSize(new Dimension(0, 250));

        // Recent bookings table
        String[] columns = {"Booking ID", "Customer", "Room", "Status"};

        Object[][] data = {
                {"HR1001", "Pradeepa", "101, 201", "Confirmed"},
                {"HR1002", "Kavya",    "102",      "Confirmed"},
                {"HR1003", "Ramesh",   "103",      "Pending"},
                {"HR1004", "Divya",    "202",      "Confirmed"},
                {"HR1005", "Arun",     "105",      "Cancelled"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        bottom.add(section("Recent Bookings", scroll));

        // Room availability with progress bars
        JPanel bars = new JPanel(new GridLayout(4, 1, 0, 12));
        bars.setOpaque(false);
        bars.add(barRow("Total Rooms",     "50",  100, new Color(52, 152, 219)));
        bars.add(barRow("Available Rooms", "32",   64, new Color(46, 204, 113)));
        bars.add(barRow("Booked Rooms",    "18",   36, new Color(255, 159, 28)));
        bars.add(barRow("Occupancy Rate",  "36%",  36, new Color(155, 89, 182)));

        bottom.add(section("Room Availability", bars));

        content.add(bottom, BorderLayout.SOUTH);

        center.add(content, BorderLayout.CENTER);

        // ================= ADD ALL =================
        main.add(header, BorderLayout.NORTH);
        main.add(side, BorderLayout.WEST);
        main.add(center, BorderLayout.CENTER);

        add(main);

        // ================= QUICK BUTTON EVENTS =================
        smart.addActionListener(e -> smartSearch());

        bill.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Room Charges : ₹5000\n" +
                        "Payment Status : Paid\n" +
                        "Total Amount : ₹5000",
                        "Generate Bill", JOptionPane.INFORMATION_MESSAGE));

        viewRooms.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Room 101 - Available\n" +
                        "Room 102 - Booked\n" +
                        "Room 201 - Available\n" +
                        "Room 202 - Available",
                        "Room Details", JOptionPane.INFORMATION_MESSAGE));

        bookings.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Total Reservations : 25\n" +
                        "Confirmed : 20\n" +
                        "Pending : 3\n" +
                        "Cancelled : 2",
                        "Booking Details", JOptionPane.INFORMATION_MESSAGE));

        customers.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Customer : Pradeepa\n" +
                        "Guests : 5\n" +
                        "Booking ID : HR1001",
                        "Customer Details", JOptionPane.INFORMATION_MESSAGE));

        addRoom.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "New Room can be added here.",
                        "Manage Rooms", JOptionPane.INFORMATION_MESSAGE));

        setVisible(true);
    }

    // ================= GRADIENT STAT CARD =================
    private JPanel card(String name, String value, Color c1, Color c2) {

        GradientPanel panel = new GradientPanel(c1, c2, 22);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));

        JLabel title = new JLabel(name);
        title.setFont(new Font(FONT, Font.BOLD, 13));
        title.setForeground(new Color(255, 255, 255, 220));

        JLabel number = new JLabel(value);
        number.setFont(new Font(FONT, Font.BOLD, 38));
        number.setForeground(Color.WHITE);

        panel.add(title, BorderLayout.NORTH);
        panel.add(number, BorderLayout.CENTER);

        return panel;
    }

    // ================= ACTION BUTTON =================
    private RoundButton action(String text, Color color) {
        RoundButton button = new RoundButton(text, color);
        button.setFont(new Font(FONT, Font.BOLD, 16));
        return button;
    }

    // ================= WHITE ROUNDED SECTION =================
    private JPanel section(String titleText, JComponent body) {
        RoundedPanel p = new RoundedPanel(Color.WHITE, 22);
        p.setLayout(new BorderLayout(0, 10));
        p.setBorder(BorderFactory.createEmptyBorder(14, 18, 16, 18));

        JLabel l = new JLabel(titleText);
        l.setFont(new Font(FONT, Font.BOLD, 18));
        l.setForeground(NAVY);

        p.add(l, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    // ================= PROGRESS BAR ROW =================
    private JPanel barRow(String label, String value, int percent, Color color) {

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(new Font(FONT, Font.BOLD, 14));
        l.setForeground(new Color(50, 60, 80));
        l.setPreferredSize(new Dimension(130, 20));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setForeground(color);
        bar.setBackground(new Color(228, 234, 242));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(10, 14));

        JPanel barWrap = new JPanel(new GridBagLayout());
        barWrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        barWrap.add(bar, gc);

        JLabel v = new JLabel(value);
        v.setFont(new Font(FONT, Font.BOLD, 16));
        v.setForeground(color.darker());
        v.setPreferredSize(new Dimension(45, 20));

        row.add(l, BorderLayout.WEST);
        row.add(barWrap, BorderLayout.CENTER);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    // ================= TABLE STYLE =================
    private void styleTable(JTable table) {

        table.setRowHeight(34);
        table.setFont(new Font(FONT, Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(200, 225, 255));
        table.setSelectionForeground(Color.BLACK);

        // Header
        JTableHeader th = table.getTableHeader();
        th.setPreferredSize(new Dimension(0, 36));
        th.setReorderingAllowed(false);
        th.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setOpaque(true);
                l.setBackground(NAVY);
                l.setForeground(Color.WHITE);
                l.setFont(new Font(FONT, Font.BOLD, 14));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                return l;
            }
        });

        // Cells (zebra rows + coloured status)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                l.setHorizontalAlignment(c == 3 ? SwingConstants.CENTER : SwingConstants.LEFT);

                if (!sel) {
                    l.setBackground(r % 2 == 0 ? Color.WHITE : new Color(243, 247, 253));
                    l.setForeground(new Color(40, 45, 65));
                }

                if (c == 3) {
                    String s = String.valueOf(v);
                    l.setFont(new Font(FONT, Font.BOLD, 14));
                    if (s.equals("Confirmed"))      l.setForeground(new Color(39, 174, 96));
                    else if (s.equals("Pending"))   l.setForeground(new Color(230, 126, 34));
                    else if (s.equals("Cancelled")) l.setForeground(new Color(231, 76, 60));
                }
                return l;
            }
        });
    }

    // ================= SMART SEARCH =================
    private void smartSearch() {

        JTextField guests = new JTextField();
        JTextField budget = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Number of Guests:"));
        panel.add(guests);
        panel.add(new JLabel("Maximum Budget:"));
        panel.add(budget);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Smart Room Search", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            try {
                int g = Integer.parseInt(guests.getText().trim());
                double b = Double.parseDouble(budget.getText().trim());

                if (g <= 2 && b >= 2000) {
                    JOptionPane.showMessageDialog(this,
                            "RECOMMENDED ROOM\n\n" +
                            "Room No : 101\n" +
                            "Capacity : 2 Guests\n" +
                            "Price : ₹2000\n" +
                            "Facilities : AC + Wi-Fi",
                            "Smart Recommendation", JOptionPane.INFORMATION_MESSAGE);

                } else if (g <= 5 && b >= 5000) {
                    JOptionPane.showMessageDialog(this,
                            "RECOMMENDED ROOM COMBINATION\n\n" +
                            "Room 101 + Room 201\n\n" +
                            "Total Capacity : 5 Guests\n" +
                            "Total Price : ₹5000\n" +
                            "Facilities : AC + Wi-Fi",
                            "Smart Recommendation", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(this,
                            "No suitable room found.",
                            "Recommendation", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Enter valid numbers.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =====================================================
    //                CUSTOM UI COMPONENTS
    // =====================================================

    // Gradient background panel (rounded corners optional)
    static class GradientPanel extends JPanel {
        Color c1, c2;
        int radius;

        GradientPanel(Color c1, Color c2, int radius) {
            this.c1 = c1;
            this.c2 = c2;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // White rounded panel with soft shadow
    static class RoundedPanel extends JPanel {
        Color color;
        int radius;

        RoundedPanel(Color color, int radius) {
            this.color = color;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(3, 4, getWidth() - 4, getHeight() - 4, radius, radius);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Rounded coloured button with hover effect
    static class RoundButton extends JButton {
        Color base;
        boolean over = false;

        RoundButton(String text, Color base) {
            super(text);
            this.base = base;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font(FONT, Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { over = true; repaint(); }
                public void mouseExited(MouseEvent e)  { over = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(over ? base.brighter() : base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Sidebar menu button (hover + active highlight)
    static class SideButton extends JButton {
        boolean active = false;
        boolean over = false;

        SideButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font(FONT, Font.BOLD, 15));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
            setPreferredSize(new Dimension(220, 48));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { over = true; repaint(); }
                public void mouseExited(MouseEvent e)  { over = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (active || over) {
                g2.setColor(HOVER);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            if (active) {
                g2.setColor(ORANGE);
                g2.fillRect(0, 0, 6, getHeight());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelDashboard());
    }
}
