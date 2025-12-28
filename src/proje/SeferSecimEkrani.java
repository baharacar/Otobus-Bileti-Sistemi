package proje;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import java.awt.Color;


public class SeferSecimEkrani extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSeferSeimEkran;
	private JTextField txtSeilebilecekUygunSeferler;
	private JButton btn_ileri;
	
	DefaultTableModel model;
	private int secilenSeferID = -1;
	private JTable table_seferler;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeferSecimEkrani frame = new SeferSecimEkrani();
					frame.setVisible(true);
				}catch (Exception e) {
						e.printStackTrace();}
			}
			});
		}
	
	public SeferSecimEkrani() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		txtSeferSeimEkran = new JTextField();
		txtSeferSeimEkran.setEditable(false);
		txtSeferSeimEkran.setHorizontalAlignment(SwingConstants.CENTER);
		txtSeferSeimEkran.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		txtSeferSeimEkran.setBounds(89, 10, 267, 41);
		txtSeferSeimEkran.setText("SEFER SEÇİM EKRANI");
		contentPane.add(txtSeferSeimEkran);
		txtSeferSeimEkran.setColumns(10);
		
		txtSeilebilecekUygunSeferler = new JTextField();
		txtSeilebilecekUygunSeferler.setEditable(false);
		txtSeilebilecekUygunSeferler.setFont(new Font("Book Antiqua", Font.BOLD, 10));
		txtSeilebilecekUygunSeferler.setText("SEÇİLEBİLECEK UYGUN SEFERLER");
		txtSeilebilecekUygunSeferler.setBounds(10, 124, 194, 18);
		contentPane.add(txtSeilebilecekUygunSeferler);
		txtSeilebilecekUygunSeferler.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 152, 414, 74);
		
		
		
		table_seferler = new JTable();
		table_seferler.setBackground(Color.WHITE);
		scrollPane.setViewportView(table_seferler);
		contentPane.add(scrollPane);
		seferleriListele(); 
		
		btn_ileri = new JButton("Koltuk seçimi için ilerle");
		btn_ileri.setVisible(false);
		btn_ileri.setBackground(Color.LIGHT_GRAY);
		btn_ileri.setBounds(123, 305, 194, 36);
		contentPane.add(btn_ileri);
			
		
		table_seferler.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		public void mouseClicked(java.awt.event.MouseEvent evt){
		int row=table_seferler.rowAtPoint(evt.getPoint());
		if(row>=0) {
			Object deger=table_seferler.getValueAt(row, 0);
			if(deger !=null) {
				try {
					secilenSeferID=Integer.parseInt(deger.toString());
					System.out.println("Seçilen ID başarılı: "+secilenSeferID);
					btn_ileri.setVisible(true);
					}catch(NumberFormatException ex) {
						System.out.println("HATA: ID hücresinde sayı yok!Gelen veri:"+deger);
						}
				}
			}
			
		txtSeferSeimEkran= new JTextField();
		
		}
			});	
		
		btn_ileri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(secilenSeferID !=-1) {
					try {
						KoltukSecimEkrani koltukEkrani=new KoltukSecimEkrani(secilenSeferID);
						koltukEkrani.setVisible(true);
						dispose();
						}
					catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Ekran geçişinde hata oluştu: "+ex.getMessage());
						}
					}else {
						JOptionPane.showMessageDialog(null, "Lütfen önce tablodan bir sefer seçiniz.");
						}
				}
			});
	
		}
		
	public void seferleriListele() {
		String[]kolonlar= {"SeferID","Güzergah","Tarih","Saat","Fiyat"};
		DefaultTableModel model = new DefaultTableModel(kolonlar,0) ;
		seferler_sql bgl =new seferler_sql();
		try {
			Connection conn = bgl.baglan();
			String sorgu= "SELECT * FROM uygun_seferler";
			
			Statement st=conn.createStatement();
			ResultSet rs= st.executeQuery(sorgu);
			
			while(rs.next()) {
				Object[] satir= {
						rs.getInt("SeferID"),
						rs.getString("Güzergah"),
						rs.getString("Tarih"),
						rs.getString("Saat"),
						rs.getDouble("Fiyat")
				};
				model.addRow(satir);
			}
			table_seferler.setModel(model);
			if(table_seferler.getColumnCount()>=6) {
				table_seferler.getColumnModel().getColumn(5).setPreferredWidth(80);
			}
			conn.close();
		}catch (Exception ex) {
					JOptionPane.showMessageDialog(null,"Veri çekme hatası: "+ex.getMessage());
					ex.printStackTrace();
					}
				}	
			}

		
