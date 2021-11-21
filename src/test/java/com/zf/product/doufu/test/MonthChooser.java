package com.zf.product.doufu.test;
 
/**
 * @date 2018-11-21
 */
  
import java.awt.BasicStroke;  
import java.awt.BorderLayout;  
import java.awt.Color;  
import java.awt.Component;  
import java.awt.Cursor;  
import java.awt.Dimension;  
import java.awt.Font;  
import java.awt.Graphics;  
import java.awt.Graphics2D;  
import java.awt.GridLayout;  
import java.awt.Point;  
import java.awt.Polygon;  
import java.awt.Stroke;  
import java.awt.Toolkit;  
import java.awt.event.FocusEvent;  
import java.awt.event.FocusListener;  
import java.awt.event.MouseAdapter;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;  
import java.awt.event.MouseMotionListener;  
import java.text.SimpleDateFormat;  
import java.util.ArrayList;  
import java.util.Calendar;  
import java.util.Comparator;  
import java.util.Date;  
import java.util.List;  
import javax.swing.BorderFactory;  
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.Popup;  
import javax.swing.PopupFactory;  
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;  
import javax.swing.event.AncestorEvent;  
import javax.swing.event.AncestorListener;  
  
/** 
 * 月份选择器，选择相应的月份 
 */  
public class MonthChooser extends JPanel{  
    private static final long serialVersionUID = 4529266044762990227L;  
    
    private Date initDate;  
    private Calendar now=Calendar.getInstance();  
    private Calendar select;  
    private Date selectDate;
    private JPanel monthPanel;//月历  
    private JP1 jp1;//四块面板,组成  
    private JP2 jp2;  
    private JP3 jp3;  
    private JP4 jp4;  
    private Font font=new Font("宋体",Font.PLAIN,12);  
    private final LabelManager lm=new LabelManager();  
    private JLabel showDate; //,toSelect;  
    private SimpleDateFormat sdf;  
    private boolean isShow=false;  
    private Popup pop;  
    
    /** 
     * Creates a new instance of DateChooser 
     */  
    public MonthChooser() {  
        this(new Date());  
    }  
    public MonthChooser(Date date){  
       this(date, "yyyy年MM月");  
    }  
    public MonthChooser(String format){  
        this(new Date(), format);  
    }  
    public MonthChooser(Date date, String format){  
        initDate=date;  
        selectDate = date;
        sdf=new SimpleDateFormat(format);  
        select=Calendar.getInstance();  
        select.setTime(initDate);  
        initPanel();  
        initLabel();  
    }  
    
    public void resetDate() {
        select.setTime(new Date());
        refresh();
    }
      
    /** 
     * 是否允许用户选择 
     */  
    public void setEnabled(boolean b){  
        super.setEnabled(b);  
        showDate.setEnabled(b);  
    }  
      
    /** 
     *得到当前选择框的日期 
     */  
    public Date getDate(){  
        return select.getTime();  
    }  
      
    //根据初始化的日期,初始化面板  
    private void initPanel(){  
        monthPanel=new JPanel(new BorderLayout());  
        monthPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));  
        JPanel up=new JPanel(new BorderLayout());  
        up.add(jp1=new JP1(),BorderLayout.NORTH);  
        //up.add(jp2=new JP2(),BorderLayout.CENTER);  
        monthPanel.add(jp3=new JP3(),BorderLayout.CENTER);  
        monthPanel.add(up,BorderLayout.NORTH);  
        monthPanel.add(jp4=new JP4(),BorderLayout.SOUTH);  
        this.addAncestorListener(new AncestorListener(){  
            public void ancestorAdded(AncestorEvent event) {  
                  
            }  
            public void ancestorRemoved(AncestorEvent event) {  
                  
            }  
            //只要祖先组件一移动,马上就让popup消失  
            public void ancestorMoved(AncestorEvent event) {  
                hidePanel();  
            }  
        });  
    }  
    
    //初始化标签  
    private void initLabel(){  
        showDate=new JLabel(sdf.format(initDate));  
        showDate.setRequestFocusEnabled(true);  
        showDate.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent me){  
                showDate.requestFocusInWindow();  
            }  
        });  
        
        this.setBackground(Color.WHITE);  
        this.add(showDate,BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(90,25));  
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));  
        showDate.addMouseListener(new MouseAdapter(){  
            public void mouseEntered(MouseEvent me){  
                if(showDate.isEnabled()){  
                    showDate.setCursor(new Cursor(Cursor.HAND_CURSOR));  
                    showDate.setForeground(Color.RED);  
                }  
            }  
            public void mouseExited(MouseEvent me){  
                if(showDate.isEnabled()){  
                    showDate.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
                    showDate.setForeground(Color.BLACK);  
                }  
            }  
            public void mousePressed(MouseEvent me){  
                if(showDate.isEnabled()){  
                    showDate.setForeground(Color.CYAN);  
                    if(isShow){  
                        hidePanel();  
                    }else{  
                        showPanel(showDate);  
                    }  
                }  
            }  
            public void mouseReleased(MouseEvent me){  
                if(showDate.isEnabled()){  
                    showDate.setForeground(Color.BLACK);  
                }  
            }  
        });  
        showDate.addFocusListener(new FocusListener(){  
            public void focusLost(FocusEvent e){  
                hidePanel();  
            }  
            public void focusGained(FocusEvent e){  
                  
            }  
        });  
    }  
    
    //根据新的日期刷新  
    private void refresh(){  
        jp1.updateDate();  
        jp3.updateDate();  
        SwingUtilities.updateComponentTreeUI(this);  
    }  
    
    //提交日期  
    private void commit(){  
        System.out.println("选中的日期是："+sdf.format(select.getTime()));  
        showDate.setText(sdf.format(select.getTime()));  
        selectDate = select.getTime();
        hidePanel();  
    }  
    
    //隐藏日期选择面板  
    private void hidePanel(){  
        if(pop!=null){  
            isShow=false;  
            pop.hide();  
            pop=null;  
        }  
    }  
    
    //显示日期选择面板  
    private void showPanel(Component owner){  
        if(pop!=null){  
            pop.hide();  
        }  
        Point show=new Point(0,showDate.getHeight());  
        SwingUtilities.convertPointToScreen(show,showDate);  
        Dimension size=Toolkit.getDefaultToolkit().getScreenSize();  
        int x=show.x;  
        int y=show.y;  
        if(x<0){  
            x=0;  
        }  
        if(x>size.width-295){  
            x=size.width-295;  
        }  
        if(y<size.height-170){  
        }else{  
            y-=188;  
        }  
        pop=PopupFactory.getSharedInstance().getPopup(owner,monthPanel,x,y);  
        pop.show();  
        isShow=true;  
    }  
    
    /** 
     * 最上面的面板用来显示月份的增减 
     */  
    private class JP1 extends JPanel{  
        JLabel yearleft,yearright,center,centercontainer;  
        public JP1(){  
            super(new BorderLayout());  
            this.setBackground(new Color(160,185,215));  
            initJP1();  
        }  
        private void initJP1(){  
            yearleft=new JLabel("  <<",JLabel.CENTER);  
            yearleft.setToolTipText("上一年");  
            yearright=new JLabel(">>  ",JLabel.CENTER);  
            yearright.setToolTipText("下一年");  
            yearleft.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));  
            yearright.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));  
              
            centercontainer=new JLabel("", JLabel.CENTER);  
            centercontainer.setLayout(new BorderLayout());  
            center=new JLabel("", JLabel.CENTER);  
   
            centercontainer.add(center,BorderLayout.CENTER);   
              
            this.add(yearleft,BorderLayout.WEST);  
            this.add(centercontainer,BorderLayout.CENTER);  
            this.add(yearright,BorderLayout.EAST);  
            this.setPreferredSize(new Dimension(295,25));  
              
            updateDate();  
              
            yearleft.addMouseListener(new MouseAdapter(){  
                public void mouseEntered(MouseEvent me){  
                    yearleft.setCursor(new Cursor(Cursor.HAND_CURSOR));  
                    yearleft.setForeground(Color.RED);  
                }  
                public void mouseExited(MouseEvent me){  
                    yearleft.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
                    yearleft.setForeground(Color.BLACK);  
                }  
                public void mousePressed(MouseEvent me){  
                    select.add(Calendar.YEAR,-1);  
                    yearleft.setForeground(Color.WHITE);  
                    refresh();  
                }  
                public void mouseReleased(MouseEvent me){  
                    yearleft.setForeground(Color.BLACK);  
                }  
            });  
            yearright.addMouseListener(new MouseAdapter(){  
                public void mouseEntered(MouseEvent me){  
                    yearright.setCursor(new Cursor(Cursor.HAND_CURSOR));  
                    yearright.setForeground(Color.RED);  
                }  
                public void mouseExited(MouseEvent me){  
                    yearright.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
                    yearright.setForeground(Color.BLACK);  
                }  
                public void mousePressed(MouseEvent me){  
                    select.add(Calendar.YEAR,1);  
                    yearright.setForeground(Color.WHITE);  
                    refresh();  
                }  
                public void mouseReleased(MouseEvent me){  
                    yearright.setForeground(Color.BLACK);  
                }  
            });  
        }  
        private void updateDate(){  
            center.setText(select.get(Calendar.YEAR)+"年");  
        }  
    }  
    
    private class JP2 extends JPanel{  
        public JP2(){  
            this.setPreferredSize(new Dimension(295,20));  
        }  
        protected void paintComponent(Graphics g){  
            g.setFont(font);  
            g.drawString("月份一 月份二 月份三 月份四 月份五 月份六",5,10);  
            g.drawLine(0,15,getWidth(),15);  
        }  
    }  
    
    private class JP3 extends JPanel{  
        public JP3(){  
            super(new GridLayout(3,4));  
            this.setPreferredSize(new Dimension(295,100));  
            initJP3();  
        }  
        private void initJP3(){  
            updateDate();  
        }  
        public void updateDate(){  
            this.removeAll();  
            lm.clear();  
            Date temp=select.getTime();  
            Calendar select=Calendar.getInstance();  
            select.setTime(temp);  
            select.set(Calendar.DAY_OF_MONTH,1);
            Calendar show=Calendar.getInstance();
            show.set(Calendar.DAY_OF_MONTH,1);
            show.set(Calendar.YEAR,select.get(Calendar.YEAR));
            show.set(Calendar.MONTH,0);
            for(int i=0;i<12;i++){  
                lm.addLabel(new MyLabel(show.get(Calendar.YEAR),  
                        show.get(Calendar.MONTH)));  
                show.add(Calendar.MONTH,1);
            }  
            for(MyLabel my:lm.getLabels()){  
                this.add(my);  
            }  
            select.setTime(temp);  
        }  
    }  
    
    private class MyLabel extends JLabel implements Comparator<MyLabel>,  
            MouseListener,MouseMotionListener{  
        private int year,month;  
        private boolean isSelected;  
        public MyLabel(int year,int month){  
            super(""+(month+1)+"月",JLabel.CENTER);  
            this.year=year;  
            this.month=month;  
            this.addMouseListener(this);  
            this.addMouseMotionListener(this);  
            this.setFont(font);  
            if(year==select.get(Calendar.YEAR)){  
                this.setForeground(Color.BLACK);  
            }else{  
                this.setForeground(Color.LIGHT_GRAY);  
            }  
            if(month==select.get(Calendar.MONTH) && year == select.get(Calendar.YEAR)){  
                this.setBackground(new Color(160,185,215));  
            }else{  
                this.setBackground(Color.WHITE);  
            }  
        }  
        public boolean getIsSelected(){  
            return isSelected;  
        }  
        public void setSelected(boolean b,boolean isDrag){  
            isSelected=b;  
            if(b&&!isDrag){  
                int temp=select.get(Calendar.MONTH);  
                select.set(year,month,1);  
                if(temp==month){  
                    SwingUtilities.updateComponentTreeUI(jp3);  
                }else{  
                    refresh();  
                }  
            }  
            this.repaint();  
        }  
        protected void paintComponent(Graphics g){  
            Calendar sd = Calendar.getInstance();
            sd.setTime(selectDate);
            if(year==sd.get(Calendar.YEAR) && month==sd.get(Calendar.MONTH) ){  
                //如果当前日期是选择日期,则高亮显示  
                g.setColor(new Color(160,185,215));  
                g.fillRect(0,0,getWidth(),getHeight());  
            }  
            
            if(year==now.get(Calendar.YEAR)&&  
                    month==now.get(Calendar.MONTH)){  
                //如果日期和当前日期一样,则用红框  
                Graphics2D gd=(Graphics2D)g;  
                gd.setColor(Color.RED);  
                Polygon p=new Polygon();  
                p.addPoint(0,0);  
                p.addPoint(getWidth()-1,0);  
                p.addPoint(getWidth()-1,getHeight()-1);  
                p.addPoint(0,getHeight()-1);  
                gd.drawPolygon(p);  
            }  
            if(isSelected){//如果被选中了就画出一个虚线框出来  
                Stroke s=new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,  
                        BasicStroke.JOIN_BEVEL,1.0f,new float[]{2.0f,2.0f},1.0f);  
                Graphics2D gd=(Graphics2D)g;  
                gd.setStroke(s);  
                gd.setColor(Color.BLACK);  
                Polygon p=new Polygon();  
                p.addPoint(0,0);  
                p.addPoint(getWidth()-1,0);  
                p.addPoint(getWidth()-1,getHeight()-1);  
                p.addPoint(0,getHeight()-1);  
                gd.drawPolygon(p);  
            }  
            super.paintComponent(g);  
        }  
        public boolean contains(Point p){  
            return this.getBounds().contains(p);  
        }  
        private void update(){  
            repaint();  
        }  
        public void mouseClicked(MouseEvent e) {  
        }  
        public void mousePressed(MouseEvent e) {  
            isSelected=true;  
            update();  
        }  
        public void mouseReleased(MouseEvent e) {  
            Point p=SwingUtilities.convertPoint(this,e.getPoint(),jp3);  
            lm.setSelect(p,false);  
            commit();  
        }  
        public void mouseEntered(MouseEvent e) {  
        }  
          
        public void mouseExited(MouseEvent e) {  
        }  
        public void mouseDragged(MouseEvent e) {  
            Point p=SwingUtilities.convertPoint(this,e.getPoint(),jp3);  
            lm.setSelect(p,true);  
        }  
        public void mouseMoved(MouseEvent e) {  
        }  
        public int compare(MyLabel o1, MyLabel o2) {  
            Calendar c1=Calendar.getInstance();  
            c1.set(o1.year,o2.month,1);  
            Calendar c2=Calendar.getInstance();  
            c2.set(o2.year,o2.month,1);  
            return c1.compareTo(c2);  
        }  
    }  
    
    private class LabelManager{  
        private List<MyLabel> list;  
        public LabelManager(){  
            list=new ArrayList<MyLabel>();  
        }  
        public List<MyLabel> getLabels(){  
            return list;  
        }  
        public void addLabel(MyLabel my){  
            list.add(my);  
        }  
        public void clear(){  
            list.clear();  
        }  
        public void setSelect(MyLabel my, boolean b){  
            for(MyLabel m:list){  
                if(m.equals(my)){  
                    m.setSelected(true,b);  
                }else{  
                    m.setSelected(false,b);  
                }  
            }  
        }  
        public void setSelect(Point p, boolean b){  
            //如果是拖动,则要优化一下,以提高效率  
            if(b){  
                //表示是否能返回,不用比较完所有的标签,能返回的标志就是把上一个标签和  
                //将要显示的标签找到了就可以了  
                boolean findPrevious=false,findNext=false;  
                for(MyLabel m:list){  
                    if(m.contains(p)){  
                        findNext=true;  
                        if(m.getIsSelected()){  
                            findPrevious=true;  
                        }else{  
                            m.setSelected(true,b);  
                        }  
                    }else if(m.getIsSelected()){  
                        findPrevious=true;  
                        m.setSelected(false,b);  
                    }  
                    if(findPrevious&&findNext){  
                        return;  
                    }  
                }  
            }else{  
                MyLabel temp=null;  
                for(MyLabel m:list){  
                    if(m.contains(p)){  
                        temp=m;  
                    }else if(m.getIsSelected()){  
                        m.setSelected(false,b);  
                    }  
                }  
                if(temp!=null){  
                    temp.setSelected(true,b);  
                }  
            }  
        }  
    }  
    
    private class JP4 extends JPanel{  
        public JP4(){  
            super(new BorderLayout());  
            this.setPreferredSize(new Dimension(295,20));  
            this.setBackground(new Color(160,185,215));  
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月");  
            final JLabel jl=new JLabel("本月: "+sdf.format(new Date()));  
            jl.setToolTipText("点击选择本月");
            jl.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(jl,BorderLayout.CENTER);  
            jl.addMouseListener(new MouseAdapter(){  
                public void mouseEntered(MouseEvent me){  
                    jl.setCursor(new Cursor(Cursor.HAND_CURSOR));  
                    jl.setForeground(Color.RED);  
                }  
                public void mouseExited(MouseEvent me){  
                    jl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
                    jl.setForeground(Color.BLACK);  
                }  
                public void mousePressed(MouseEvent me){  
                    jl.setForeground(Color.WHITE);  
                    select.setTime(new Date());  
                    refresh();  
                    commit();  
                }  
                public void mouseReleased(MouseEvent me){  
                    jl.setForeground(Color.BLACK);  
                }  
            });  
        }  
    }  
    
    public static void main(String[] args) {  
        final MonthChooser mp = new MonthChooser("yyyy年MM月");  
        JFrame jf = new JFrame("测试月份选择器");  
        jf.add(mp, BorderLayout.CENTER);  
        jf.add(new JButton("测试用的"),BorderLayout.NORTH);  
        jf.pack();  
        jf.setLocationRelativeTo(null);  
        jf.setVisible(true);  
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  