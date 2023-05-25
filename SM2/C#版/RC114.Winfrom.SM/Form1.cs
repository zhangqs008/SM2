using System;
using System.Text;
using System.Windows.Forms;

namespace RC114.Winfrom.SM
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
        }

        private void btnGenerateKey_Click(object sender, EventArgs e)
        {
            var dic = Sm2Util.GenerateKeyPair();
            txtPubKey.Text = dic["pubkey"];
            txtPriKey.Text = dic["prikey"];
        }

        private void btnEncode_Click(object sender, EventArgs e)
        {
            var pubkey = txtPubKey.Text.Trim(); //公钥加密
            var prikey = txtPriKey.Text.Trim(); //私钥解密
            if (pubkey.Length == 0)
            {
                MessageBox.Show("请填入加密公钥！", "提示信息", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            var input = txtEncodeInput.Text.Trim();
            if (input.Length == 0)
            {
                MessageBox.Show("请输入需加密的明文！", "提示信息", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            try
            {
                var util = new Sm2Util(pubkey, prikey);
                var output = Sm2Util.BytesToHexStr(util.Encrypt(Encoding.Default.GetBytes(input)));
                txtEncodeOutput.Text = output;
                txtDecodeInput.Text = output;
            }
            catch (Exception ex)
            {
                txtEncodeOutput.Text = ex.Message;
            }
        }


        private void btnDecode_Click(object sender, EventArgs e)
        {
            var pubkey = txtPubKey.Text.Trim(); //公钥加密
            var prikey = txtPriKey.Text.Trim(); //私钥解密
            if (prikey.Length == 0)
            {
                MessageBox.Show("请填入解密私钥！", "提示信息", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            var input = txtDecodeInput.Text.Trim();
            if (input.Length == 0)
            {
                MessageBox.Show("请输入需解密的密文！", "提示信息", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            try
            {
                var util = new Sm2Util(pubkey, prikey);
                var output = Encoding.UTF8.GetString(util.Decrypt(Sm2Util.HexStrToBytes(input)));
                txtDecodeOutput.Text = output;
            }
            catch (Exception ex)
            {
                txtDecodeOutput.Text = ex.Message;
            }
        }


        private void lblStatus_Click(object sender, EventArgs e)
        {
            Help.ShowHelp(this, "https://the-x.cn/cryptography/Sm2.aspx");
        }
    }
}