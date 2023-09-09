package com.dialog;

import com.Main;
import com.base.BaseDialog;
import com.base.BaseFragment;
import com.bean.IdCard;
import com.db.SqlUser;
import com.ui.MyButton;
import com.ui.MyLabel;
import com.ui.MyTextField;
import com.utils.ChangeUtiles;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class IdCardDialog extends BaseDialog {

    private JTextField idNumberText;
    private JTextField idName;
    private JTextField idSex;
    private JTextField idBirthday;
    private JButton okButton;
    private BaseFragment informationFragment;
    private int CODE;
    private IdCard idCard = null;

    public static int CHANGE = 1;
    public static int ADD = 0;

    public IdCardDialog(BaseFragment informationFragment, int CODE) {
        this.informationFragment = informationFragment;
        this.CODE = CODE;
    }

    public IdCardDialog(BaseFragment informationFragment, IdCard idCard, int CODE) {
        this.informationFragment = informationFragment;
        this.idCard = idCard;
        this.CODE = CODE;
    }

    @Override
    public void initView() {
        int x_star = 30;
        int y_star = 40;
        int mergin = 60;

        JLabel numberTitle = new MyLabel("身份证号", x_star, y_star, 80, 30, textFont);
        idNumberText = new MyTextField(x_star + 80, y_star, 150, 40, titleFont);
        if (CODE == CHANGE) {
            idNumberText.setText(idCard.getIdCardNumber());
            idNumberText.setEnabled(false);
        }

        JLabel nameTitle = new MyLabel("姓名", x_star, y_star += mergin, 80, 30, textFont);
        idName = new MyTextField(x_star + 80, y_star, 150, 40, titleFont);

        JLabel sexTitle = new MyLabel("性别", x_star, y_star += mergin, 80, 30, textFont);
        idSex = new MyTextField(x_star + 80, y_star, 150, 40, titleFont);

        JLabel birthdayTitle = new MyLabel("出生日期", x_star, y_star += mergin, 80, 30, textFont);
        idBirthday = new MyTextField(x_star + 80, y_star, 150, 40, titleFont);

        okButton = new MyButton("添加", x_star + 75, y_star += mergin + 10, 100, 40, textFont, 1);
        if (CODE == CHANGE) {
            okButton.setText("确认修改");
        }

        myFrame.add(idName);
        myFrame.add(okButton);
        myFrame.add(idBirthday);
        myFrame.add(idSex);
        myFrame.add(idNumberText);
        myFrame.add(sexTitle);
        myFrame.add(birthdayTitle);
        myFrame.add(nameTitle);
        myFrame.add(numberTitle);
        initFrame("images//idcard_bg.jpg");
    }

    @Override
    public SqlUser initSqlUser() {
        return SqlUser.newInstance(Main.user.getType());
    }

    @Override
    public void loadData() {

    }

    @Override
    public void addListener() {
        if (CODE == ADD) {
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (getSqlHelper().insertIdCard(getIdCard(), Main.user)) {
                        showMessageDialog("增添成功");
                        myFrame.dispose();
                    } else {
                        showMessageDialog("增添失败");
                    }
                    informationFragment.loadData();
                }
            });
        } else {
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (getIdCard().getName().equals(" ")) {
                        showMessageDialog("增添失败");
                    } else {
                        if (getSqlHelper().changeIdCard(getIdCard())) {
                            showMessageDialog("修改成功");
                            myFrame.dispose();
                            informationFragment.loadData();
                        } else {
                            showMessageDialog("修改失败");
                        }
                    }
                }
            });
        }
    }

    private IdCard getIdCard() {
        String number = idNumberText.getText();
        String name = idName.getText();
        String sex = idSex.getText();
        Date birthday = ChangeUtiles.createDate(idBirthday.getText());
        if (number.length() < 10) {
            showMessageDialog("身份证号输入错误");
        } else if (name.length() < 1) {
            showMessageDialog("名字输入错误");
        } else if (sex.length() < 1) {
            showMessageDialog("性别输入错误");
        } else if (idBirthday.getText().length() < 3) {
            showMessageDialog("出生日期输入错误");
        } else {
            return new IdCard(number, name, sex, birthday);
        }
        return new IdCard(" ", " ", " ", birthday);
    }

}
