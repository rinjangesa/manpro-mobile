package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PronunciationActivity extends AppCompatActivity {

    private String wallData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation);

        final String[] title = {
                "Vokal Panjang",
                "Bunyi yang ditulis dengan huruf ん",
                "Bunyi yang ditulis dengan huruf っ",
                "Bunyi yang ditulis dengan huruf ゃ、ゅ、ょ"
        };

        final String[] desc = {
                "<!DOCTYPE html><html><head></head><body><p>Vokal Bahasa Jepang ada lima macam, yaitu あ、い、う、え、お, di samping itu vokal yang diucapkan bunyi yang panjang disebut vokal panjang. Vokal diucapkan satu mora sedangkan vokal panjang diucapkan dua mora. Berdasarkan panjang atau pendeknya vokal akan menimbulkan perbedaan arti kata.</p><p>Contoh :</p><table style=\"border-collapse: collapse; width: 100%; height: 126px;\" border=\"1\"><tbody><tr style=\"height: 18px;\"><td style=\"width: 50%; text-align: center; height: 18px;\">Pendek</td><td style=\"width: 50%; text-align: center; height: 18px;\">Panjang</td></tr><tr style=\"height: 19px;\"><td style=\"width: 50%; text-align: center; height: 19px;\">おばさん (tante)</td><td style=\"width: 50%; text-align: center; height: 19px;\">おば<span style=\"text-decoration: underline;\">あ</span>さん (nenek)</td></tr><tr style=\"height: 17px;\"><td style=\"width: 50%; text-align: center; height: 17px;\">おじさん (om)</td><td style=\"width: 50%; text-align: center; height: 17px;\">おじ<span style=\"text-decoration: underline;\">い</span>さん</td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; text-align: center; height: 18px;\">ゆき (salju)</td><td style=\"width: 50%; text-align: center; height: 18px;\">ゆ<span style=\"text-decoration: underline;\">う</span>き (keberanian)</td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; text-align: center; height: 18px;\">え (gambar)</td><td style=\"width: 50%; text-align: center; height: 18px;\">え<span style=\"text-decoration: underline;\">え</span> (ya)</td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; text-align: center; height: 18px;\">とる (mengambil)</td><td style=\"width: 50%; text-align: center; height: 18px;\">と<span style=\"text-decoration: underline;\">お</span>る (melewati)</td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; text-align: center; height: 18px;\">ここ (disini)</td><td style=\"width: 50%; text-align: center; height: 18px;\">こ<span style=\"text-decoration: underline;\">う</span>こ<span style=\"text-decoration: underline;\">う</span> (SMA)</td></tr></tbody></table><p>[Perhatian]</p><p>1) Cara menulis <em>Hiragana</em></p><ul><li>Bunyi panjang dari kolom あ、い、う masing-masing dibubuhi あ、い、う.</li><li>Bunyi panjang dari kolom え dibubuhi い.</li><li>Bunyi panjang dari kolom お dibubuhi う.</li></ul><p>2) Cara menulis <em>Katakana</em></p><p><em>-</em> Menggunakan tanda ー sebagai tanda bunyi panjang.</p></body></html>",
                "<!DOCTYPE html><html><head></head><body><p><span style=\"color: #ff0000;\">ん tidak terdapat di awal kata&nbsp;<span style=\"color: #000000;\">dan merupakan bunyi yang memiliki satu mora. ん berubah menjadi bunyi yang mudah diucapkan seperti /n//m//ng, karena terpengaruh oleh bunyi yang terletak dibelakangnya.</span></span></p><p>1) Diucapkan sebagai /n/ di depan bunyi Baris た, baris だ, baris ら, dan baris な.</p><p>Contoh :</p><ul><li>は<span style=\"text-decoration: underline;\">ん</span>たい (perlawanan)</li><li>み<span style=\"text-decoration: underline;\">ん</span>な (semua)</li><li>う<span style=\"text-decoration: underline;\">ん</span>どう (gerakan)</li></ul><p>2) Diucapkan sebagai /m/ di depan bunyi baris ば, baris　ぱ dan baris ま.</p><p>Contoh :&nbsp;</p><ul><li>し<span style=\"text-decoration: underline;\">ん</span>ぶん (koran)</li><li>え<span style=\"text-decoration: underline;\">ん</span>ぴつ (pensil)</li><li>う<span style=\"text-decoration: underline;\">ん</span>めい (nasib)</li></ul><p>3) Diucapkan sebagai /ng/ di depan bunyi baris か dan baris が.</p><p>Contoh :&nbsp;</p><ul><li>て<span style=\"text-decoration: underline;\">ん</span>き (cuaca)</li><li>け<span style=\"text-decoration: underline;\">ん</span>かく (peninjauan)</li></ul></body></html>",
                "<!DOCTYPE html><html><head></head><body><p>っ memiliki bunyi yang satu mora, dan muncul di depan bunyi baris か, baris き, baris た, dan baris ば. Jika menulis kata-kata dari bahasa asing, digunakan pula di depan bunyi baris ザ, baris ダ dan lain-lainnya.</p><p>Contoh :&nbsp;</p><ul><li>ぶか (bawahan)&nbsp;&ne; ぶっか (harga barang)</li><li>かさい (kebarakan)&nbsp;&ne; かっさい (tepukan)</li><li>おと (bunyi)&nbsp;&ne; おっと (suami)</li><li>に<span style=\"text-decoration: underline;\">っ</span>き (buku harian)</li><li>い<span style=\"text-decoration: underline;\">っ</span>ぱい (penuh)</li><li>ざっし (majalah)</li><li>きって (prangko)</li><li>コップ (gelas)</li><li>ベッド (ranjang)</li></ul></body></html>",
                "<!DOCTYPE html><html><head></head><body><p>Bunyi yang dibutuhkan ゃ、ゅ、ょ dengan huruf kecil disebut <em>Yoon.</em> <em>Yoon</em> terdiri dari dua huruf, tetapi diucapkan satu mora.</p><p>Contoh :&nbsp;</p><ul><li>ひやく (melompat)&nbsp;&ne; <span style=\"text-decoration: underline;\">ひゃ</span>く (seratus)</li><li>じゆう (kebebasan)&nbsp;&ne; <span style=\"text-decoration: underline;\">じゅ</span>う (sepuluh)</li><li>びよういん (salon kecantikan)&nbsp;&ne; <span style=\"text-decoration: underline;\">びょ</span>ういん (rumah sakit)</li><li><span style=\"text-decoration: underline;\">シャ</span>ツ (kemeja)</li><li>お<span style=\"text-decoration: underline;\">ちゃ</span> (teh)</li><li><span style=\"text-decoration: underline;\">ぎゅにゅ</span> (susu sapi)</li><li><span style=\"text-decoration: underline;\">きょ</span>う (hari ini)</li><li>ぶ<span style=\"text-decoration: underline;\">ちょ</span>う (kepala bagian)</li><li><span style=\"text-decoration: underline;\">りょ</span>こう (perjalanan)</li></ul></body></html>"
        };

        wallData = getIntent().getStringExtra("WALLPAPER");

        ImageView wallView = findViewById(R.id.pronuncation_list_wallpaper);
        Button backBtn = findViewById(R.id.back_pronunciation);
        Button oneBtn = findViewById(R.id.vokal_panjang_btn);
        Button twoBtn = findViewById(R.id.dengan_n_btn);
        Button threeBtn = findViewById(R.id.dengan_tsu_kecil_btn);
        Button fourBtn = findViewById(R.id.dengan_y_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PronunciationActivity.this, DetailPronunciationActivity.class);
                intent.putExtra("DETAIL", "1");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("TITLE", title[0]);
                intent.putExtra("DESC", desc[0]);
                startActivity(intent);
            }
        });

        twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PronunciationActivity.this, DetailPronunciationActivity.class);
                intent.putExtra("DETAIL", "2");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("TITLE", title[1]);
                intent.putExtra("DESC", desc[1]);
                startActivity(intent);
            }
        });

        threeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PronunciationActivity.this, DetailPronunciationActivity.class);
                intent.putExtra("DETAIL", "3");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("TITLE", title[2]);
                intent.putExtra("DESC", desc[2]);
                startActivity(intent);
            }
        });

        fourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PronunciationActivity.this, DetailPronunciationActivity.class);
                intent.putExtra("DETAIL", "4");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("TITLE", title[3]);
                intent.putExtra("DESC", desc[3]);
                startActivity(intent);
            }
        });

        Glide.with(PronunciationActivity.this).load(wallData).into(wallView);
    }
}
