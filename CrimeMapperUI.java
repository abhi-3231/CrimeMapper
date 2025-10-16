import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CrimeMapperUI extends JFrame {
    private JTable dataTable;
    private JComboBox<String> cityFilter, severityFilter, typeFilter;
    private JTextField searchField;
    private MapPanel mapPanel;

    public CrimeMapperUI() {
        setTitle("Crime Mapper Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createNavBar(), BorderLayout.NORTH);
        add(createTabbedPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navBar.setBackground(new Color(40, 60, 90));

        JLabel title = new JLabel("Crime Mapper");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        navBar.add(title);

        JButton refreshButton = new JButton("Refresh");
        JButton exitButton = new JButton("Exit");

        refreshButton.addActionListener(e -> refreshData());
        exitButton.addActionListener(e -> System.exit(0));

        navBar.add(refreshButton);
        navBar.add(exitButton);

        return navBar;
    }

    // 🔹 Main Tabbed Pane (Dashboard + Map)
    private JTabbedPane createTabbedPanel() {
        JTabbedPane tabs = new JTabbedPane();

        JPanel dashboardPanel = createDashboardPanel();
        JPanel mapTabPanel = createMapOnlyPanel();

        tabs.addTab("📊 Dashboard", dashboardPanel);
        tabs.addTab("🗺 Map View", mapTabPanel);

        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        return tabs;
    }

    // ---------- DASHBOARD TAB ----------
    private JPanel createDashboardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top Graph
        GraphPanel graphPanel = new GraphPanel();
        graphPanel.setPreferredSize(new Dimension(1100, 220));
        graphPanel.setBorder(BorderFactory.createTitledBorder("Crime Statistics Overview"));
        mainPanel.add(graphPanel, BorderLayout.NORTH);

        // Filters + Table below
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createFilterPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    // ---------- MAP TAB ----------
    private JPanel createMapOnlyPanel() {
        String[] columns = {"ID", "City", "Type", "Severity", "Date"};
        Object[][] data = {
                {"001", "Mumbai", "Theft", "Low", "2025-01-01"},
                {"002", "Delhi", "Assault", "High", "2025-02-05"},
                {"003", "Bangalore", "Burglary", "Medium", "2025-03-10"},
                {"004", "Kolkata", "Theft", "Low", "2025-04-15"},
                {"005", "Chennai", "Assault", "High", "2025-05-21"},
        };

        JPanel mapTab = new JPanel(new BorderLayout());
        mapPanel = new MapPanel(data);
        mapPanel.setBorder(new TitledBorder("Interactive Map View"));
        mapTab.add(mapPanel, BorderLayout.CENTER);

        return mapTab;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(new TitledBorder("Filters"));

        filterPanel.add(new JLabel("City:"));
        cityFilter = new JComboBox<>(new String[]{"All", "Mumbai", "Delhi", "Bangalore", "Kolkata", "Chennai"});
        filterPanel.add(cityFilter);

        filterPanel.add(new JLabel("Severity:"));
        severityFilter = new JComboBox<>(new String[]{"All", "Low", "Medium", "High"});
        filterPanel.add(severityFilter);

        filterPanel.add(new JLabel("Type:"));
        typeFilter = new JComboBox<>(new String[]{"All", "Theft", "Assault", "Burglary"});
        filterPanel.add(typeFilter);

        filterPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        filterPanel.add(searchField);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyFilters());
        filterPanel.add(applyButton);

        return filterPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "City", "Type", "Severity", "Date"};
        Object[][] data = {
                {"001", "Mumbai", "Theft", "Low", "2025-01-01"},
                {"002", "Delhi", "Assault", "High", "2025-02-05"},
                {"003", "Bangalore", "Burglary", "Medium", "2025-03-10"},
                {"004", "Kolkata", "Theft", "Low", "2025-04-15"},
                {"005", "Chennai", "Assault", "High", "2025-05-21"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        dataTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(dataTable);
        tableScroll.setPreferredSize(new Dimension(1000, 300));
        tableScroll.setBorder(new TitledBorder("Incident Records"));

        return tableScroll;
    }

    private void refreshData() {
        JOptionPane.showMessageDialog(this, "Data refreshed!");
        if (mapPanel != null) mapPanel.repaint();
    }

    private void applyFilters() {
        JOptionPane.showMessageDialog(this, "Filters applied!");
        if (mapPanel != null) mapPanel.repaint();
    }

    // ---------- GRAPH PANEL ----------
    class GraphPanel extends JPanel {
        Map<String, Integer> data;

        public GraphPanel() {
            data = new LinkedHashMap<>();
            data.put("Theft", 35);
            data.put("Assault", 25);
            data.put("Burglary", 18);
            data.put("Robbery", 12);
            data.put("Fraud", 8);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int x = 80;
            int baseY = height - 60;
            int barWidth = 60;
            int spacing = 50;

            int max = Collections.max(data.values());

            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(50, baseY, width - 50, baseY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("Incident Type →", width / 2 - 40, baseY + 30);
            g2.drawString("↑ Count", 10, 30);

            for (Map.Entry<String, Integer> e : data.entrySet()) {
                int val = e.getValue();
                int barHeight = (int) ((height - 100) * (val / (double) max));

                GradientPaint gradient = new GradientPaint(
                        x, baseY - barHeight,
                        new Color(80, 140, 240),
                        x, baseY,
                        new Color(160, 190, 255));
                g2.setPaint(gradient);
                g2.fillRect(x, baseY - barHeight, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                g2.drawRect(x, baseY - barHeight, barWidth, barHeight);
                g2.drawString(e.getKey(), x + 10, baseY + 20);
                g2.drawString(String.valueOf(val), x + 20, baseY - barHeight - 5);

                x += barWidth + spacing;
            }
        }
    }

    // ---------- MAP PANEL ----------
    class MapPanel extends JPanel {
        Object[][] data;
        Random rand = new Random();
        java.util.List<Point> points = new ArrayList<>();

        public MapPanel(Object[][] data) {
            this.data = data;
            generatePoints();
            setToolTipText("");

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    for (int i = 0; i < points.size(); i++) {
                        Point p = points.get(i);
                        if (p.distance(e.getPoint()) < 10) {
                            Object[] row = data[i];
                            setToolTipText("<html><b>City:</b> " + row[1] +
                                    "<br><b>Type:</b> " + row[2] +
                                    "<br><b>Severity:</b> " + row[3] +
                                    "<br><b>Date:</b> " + row[4] + "</html>");
                            return;
                        }
                    }
                    setToolTipText(null);
                }
            });
        }

        private void generatePoints() {
            points.clear();
            for (int i = 0; i < data.length; i++) {
                int x = 120 + rand.nextInt(850);
                int y = 60 + rand.nextInt(400);
                points.add(new Point(x, y));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(230, 240, 255));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString("🗺 Crime Hotspot Map", 20, 30);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            for (int i = 0; i < data.length; i++) {
                String city = data[i][1].toString();
                String severity = data[i][3].toString();
                Color color = switch (severity) {
                    case "High" -> new Color(255, 80, 80);
                    case "Medium" -> new Color(255, 180, 70);
                    default -> new Color(90, 200, 90);
                };
                Point p = points.get(i);

                g2.setColor(color);
                g2.fillOval(p.x - 6, p.y - 6, 12, 12);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - 6, p.y - 6, 12, 12);

                // 🏷 Add City/State Name beside dot
                g2.drawString(city, p.x + 12, p.y + 5);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CrimeMapperUI::new);
    }
}