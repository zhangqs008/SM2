
namespace RC114.Winfrom.SM
{
    partial class MainForm
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要修改
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.btnGenerateKey = new System.Windows.Forms.Button();
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.lblStatus = new System.Windows.Forms.ToolStripStatusLabel();
            this.txtPubKey = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.txtPriKey = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.txtEncodeInput = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.btnEncode = new System.Windows.Forms.Button();
            this.label7 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.btnDecode = new System.Windows.Forms.Button();
            this.label9 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.txtDecodeOutput = new System.Windows.Forms.TextBox();
            this.label11 = new System.Windows.Forms.Label();
            this.txtDecodeInput = new System.Windows.Forms.TextBox();
            this.txtEncodeOutput = new System.Windows.Forms.TextBox();
            this.label12 = new System.Windows.Forms.Label();
            this.label13 = new System.Windows.Forms.Label();
            this.statusStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnGenerateKey
            // 
            this.btnGenerateKey.Location = new System.Drawing.Point(67, 96);
            this.btnGenerateKey.Name = "btnGenerateKey";
            this.btnGenerateKey.Size = new System.Drawing.Size(104, 23);
            this.btnGenerateKey.TabIndex = 0;
            this.btnGenerateKey.Text = "生成公钥和私钥";
            this.btnGenerateKey.UseVisualStyleBackColor = true;
            this.btnGenerateKey.Click += new System.EventHandler(this.btnGenerateKey_Click);
            // 
            // statusStrip1
            // 
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.lblStatus});
            this.statusStrip1.Location = new System.Drawing.Point(0, 607);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Size = new System.Drawing.Size(959, 22);
            this.statusStrip1.TabIndex = 1;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // lblStatus
            // 
            this.lblStatus.Name = "lblStatus";
            this.lblStatus.Size = new System.Drawing.Size(80, 17);
            this.lblStatus.Text = "在线验证工具";
            this.lblStatus.Click += new System.EventHandler(this.lblStatus_Click);
            // 
            // txtPubKey
            // 
            this.txtPubKey.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtPubKey.Location = new System.Drawing.Point(67, 42);
            this.txtPubKey.Name = "txtPubKey";
            this.txtPubKey.Size = new System.Drawing.Size(866, 21);
            this.txtPubKey.TabIndex = 2;
            this.txtPubKey.Text = "04F59485B23304990ED45E42521BE504D0DE358B9E4031A172EF48700071AF985A8EA8B12BB479E24" +
    "152814EE61840932BFFF5B3B1657C9CF50A61756B1D901E1C";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(20, 46);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 12);
            this.label1.TabIndex = 3;
            this.label1.Text = "公钥：";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(20, 73);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(41, 12);
            this.label2.TabIndex = 5;
            this.label2.Text = "私钥：";
            // 
            // txtPriKey
            // 
            this.txtPriKey.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtPriKey.Location = new System.Drawing.Point(67, 69);
            this.txtPriKey.Name = "txtPriKey";
            this.txtPriKey.Size = new System.Drawing.Size(866, 21);
            this.txtPriKey.TabIndex = 4;
            this.txtPriKey.Text = "78AEBAE7DE025B6954357DB327F4AE412B3657B1E1ED36F89927C065155DBA9A";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.ForeColor = System.Drawing.Color.Gray;
            this.label3.Location = new System.Drawing.Point(123, 140);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(77, 12);
            this.label3.TabIndex = 6;
            this.label3.Text = "（公钥加密）";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.ForeColor = System.Drawing.Color.Gray;
            this.label4.Location = new System.Drawing.Point(124, 346);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(77, 12);
            this.label4.TabIndex = 7;
            this.label4.Text = "（私钥解密）";
            // 
            // txtEncodeInput
            // 
            this.txtEncodeInput.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtEncodeInput.Location = new System.Drawing.Point(67, 164);
            this.txtEncodeInput.Name = "txtEncodeInput";
            this.txtEncodeInput.Size = new System.Drawing.Size(866, 21);
            this.txtEncodeInput.TabIndex = 8;
            this.txtEncodeInput.Text = "asdcdefg1234567890!@#$%^&*()";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(20, 169);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(41, 12);
            this.label5.TabIndex = 9;
            this.label5.Text = "明文：";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(20, 199);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(41, 12);
            this.label6.TabIndex = 10;
            this.label6.Text = "密文：";
            // 
            // btnEncode
            // 
            this.btnEncode.Location = new System.Drawing.Point(67, 288);
            this.btnEncode.Name = "btnEncode";
            this.btnEncode.Size = new System.Drawing.Size(56, 23);
            this.btnEncode.TabIndex = 12;
            this.btnEncode.Text = "加密";
            this.btnEncode.UseVisualStyleBackColor = true;
            this.btnEncode.Click += new System.EventHandler(this.btnEncode_Click);
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label7.Location = new System.Drawing.Point(20, 15);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(142, 12);
            this.label7.TabIndex = 13;
            this.label7.Text = "第1步：生成公钥和私钥";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label8.Location = new System.Drawing.Point(20, 139);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(103, 12);
            this.label8.TabIndex = 14;
            this.label8.Text = "第2步：测试加密";
            // 
            // btnDecode
            // 
            this.btnDecode.Location = new System.Drawing.Point(67, 493);
            this.btnDecode.Name = "btnDecode";
            this.btnDecode.Size = new System.Drawing.Size(56, 23);
            this.btnDecode.TabIndex = 19;
            this.btnDecode.Text = "解密";
            this.btnDecode.UseVisualStyleBackColor = true;
            this.btnDecode.Click += new System.EventHandler(this.btnDecode_Click);
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(20, 381);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(41, 12);
            this.label9.TabIndex = 17;
            this.label9.Text = "密文：";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.Location = new System.Drawing.Point(20, 467);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(41, 12);
            this.label10.TabIndex = 16;
            this.label10.Text = "明文：";
            // 
            // txtDecodeOutput
            // 
            this.txtDecodeOutput.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtDecodeOutput.Location = new System.Drawing.Point(67, 462);
            this.txtDecodeOutput.Name = "txtDecodeOutput";
            this.txtDecodeOutput.Size = new System.Drawing.Size(866, 21);
            this.txtDecodeOutput.TabIndex = 15;
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label11.Location = new System.Drawing.Point(20, 345);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(103, 12);
            this.label11.TabIndex = 20;
            this.label11.Text = "第3步：测试解密";
            // 
            // txtDecodeInput
            // 
            this.txtDecodeInput.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtDecodeInput.Location = new System.Drawing.Point(67, 378);
            this.txtDecodeInput.Multiline = true;
            this.txtDecodeInput.Name = "txtDecodeInput";
            this.txtDecodeInput.Size = new System.Drawing.Size(866, 78);
            this.txtDecodeInput.TabIndex = 21;
            // 
            // txtEncodeOutput
            // 
            this.txtEncodeOutput.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtEncodeOutput.Location = new System.Drawing.Point(67, 196);
            this.txtEncodeOutput.Multiline = true;
            this.txtEncodeOutput.Name = "txtEncodeOutput";
            this.txtEncodeOutput.Size = new System.Drawing.Size(866, 78);
            this.txtEncodeOutput.TabIndex = 22;
            // 
            // label12
            // 
            this.label12.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.label12.AutoSize = true;
            this.label12.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label12.ForeColor = System.Drawing.Color.Green;
            this.label12.Location = new System.Drawing.Point(547, 290);
            this.label12.Name = "label12";
            this.label12.Size = new System.Drawing.Size(384, 68);
            this.label12.TabIndex = 23;
            this.label12.Text = "加密过程：\r\n1.先将待加密字符串(string)转换为byte[]。\r\n2.再对byte[]进行加密运算，返回16进制的字节数组byte[]。\r\n3.最后将加密" +
    "后的16进制byte[]，转换为16进制的原码字符串(string)。";
            // 
            // label13
            // 
            this.label13.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.label13.AutoSize = true;
            this.label13.Font = new System.Drawing.Font("微软雅黑", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label13.ForeColor = System.Drawing.Color.Green;
            this.label13.Location = new System.Drawing.Point(547, 493);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(312, 68);
            this.label13.TabIndex = 24;
            this.label13.Text = "解密过程：\r\n1.先将待解密的16进制原码字符串转字节数组byte[]。\r\n2.再对byte[]进行解密运算，返回解密文本对应的btye[]。\r\n3.最后将解密后" +
    "的byte[]，再转换为普通字符串(string)。";
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(959, 629);
            this.Controls.Add(this.label13);
            this.Controls.Add(this.label12);
            this.Controls.Add(this.txtEncodeOutput);
            this.Controls.Add(this.txtDecodeInput);
            this.Controls.Add(this.label11);
            this.Controls.Add(this.btnDecode);
            this.Controls.Add(this.label9);
            this.Controls.Add(this.label10);
            this.Controls.Add(this.txtDecodeOutput);
            this.Controls.Add(this.label8);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.btnEncode);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.txtEncodeInput);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.txtPriKey);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.txtPubKey);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.btnGenerateKey);
            this.Name = "MainForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "国密SM2算法辅助工具";
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnGenerateKey;
        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel lblStatus;
        private System.Windows.Forms.TextBox txtPubKey;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox txtPriKey;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox txtEncodeInput;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Button btnEncode;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Button btnDecode;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.TextBox txtDecodeOutput;
        private System.Windows.Forms.Label label11;
        private System.Windows.Forms.TextBox txtDecodeInput;
        private System.Windows.Forms.TextBox txtEncodeOutput;
        private System.Windows.Forms.Label label12;
        private System.Windows.Forms.Label label13;
    }
}

