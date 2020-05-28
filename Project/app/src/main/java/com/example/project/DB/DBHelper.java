package com.example.project.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.R;
import com.example.project.game.SudokuGame;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;

    private Context mContext;

    DBHelper(Context context) {
        super(context, "SudokuGame", null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Sudoku (id INTEGER PRIMARY KEY, levelId INTEGER, data Text);");

        insertSudoku(db,1, 1, "052006000160900004049803620400000800083201590001000002097305240200009056000100970");
        insertSudoku(db,2, 1, "052400100100002030000813025400007010683000597070500002890365000010700006006004970");
        insertSudoku(db,3, 1, "302000089068052734009000000400007000083201590000500002000000200214780350530000908");
        insertSudoku(db,4, 1, "402000007000080420050302006090030050503060708070010060900406030015070000200000809");
        insertSudoku(db,5, 1, "060091080109680405050040106600000200023904710004000003907020030305079602040150070");
        insertSudoku(db,6, 1, "060090380009080405050300106001008200020904010004200900907006030305070600046050070");
        insertSudoku(db,7, 1, "402000380109607400008300106090030004023964710800010060907006500005809602046000809");
        insertSudoku(db,8, 1, "400091000009007425058340190691000000003964700000000963087026530315800600000150009");
        insertSudoku(db,9, 1, "380001004002600070000487003000040239201000406495060000600854000070006800800700092");
        insertSudoku(db,10, 1, "007520060002009008006407000768005009031000450400300781000804300100200800050013600");
        insertSudoku(db,11, 1, "380000000540009078000407503000145209000908000405362000609804000170200045000000092");
        insertSudoku(db,12, 1, "007001000540609078900487000760100230230000056095002081000854007170206045000700600");
        insertSudoku(db,13, 1, "007021900502009078006407500000140039031908450490062000009804300170200805004710600");
        insertSudoku(db,14, 1, "086500204407008090350009000009080601010000080608090300000200076030800409105004820");
        insertSudoku(db,15, 1, "086507000007360100000000068249003050500000007070100342890000000002056400000904820");
        insertSudoku(db,16, 1, "000007230420368000050029768000080650000602000078090000894230070000856019065900000");
        insertSudoku(db,17, 1, "906000200400368190350400000209080051013040980670090302000001076032856009005000803");
        insertSudoku(db,18, 1, "095002000700804001810076500476000302000000000301000857003290075500307006000400130");
        insertSudoku(db,19, 1, "005002740002850901810000500070501302008723600301609050003000075509017200087400100");
        insertSudoku(db,20, 1, "605102740732004001000000020400501300008020600001609007060000000500300286087405109");
        insertSudoku(db,21, 1, "695102040700800000000970023076000090900020004020000850160098000000007006080405139");
        insertSudoku(db,22, 1, "090002748000004901800906500470500090008000600020009057003208005509300000287400030");
        insertSudoku(db,23, 1, "001009048089070030003106005390000500058602170007000094900708300030040860870300400");
        insertSudoku(db,24, 1, "600039708000004600000100025002017506408000103107850200910008000005900000806320009");
        insertSudoku(db,25, 1, "620500700500270631040100005302000086000090000160000204900008050235041007006005019");
        insertSudoku(db,26, 1, "080130002140902007273080000000070206007203900502040000000060318600308024400021050");
        insertSudoku(db,27, 1, "980100402046950000200684001010009086007000900590800070700465008000098720408001059");
        insertSudoku(db,28, 1, "085100400000950007073684001010070080067203940090040070700465310600098000008001650");
        insertSudoku(db,29, 1, "085100460146000807070004001300009080067000940090800003700400010601000724038001650");
        insertSudoku(db,30, 1, "085130462006000007270680090000009200060213040002800000020065018600000700438021650");
        insertSudoku(db,31, 2, "916004072800620050500008930060000200000207000005000090097800003080076009450100687");
        insertSudoku(db,32, 2, "000900082063001409908000000000670300046050290007023000000000701704300620630007000");
        insertSudoku(db,33, 2, "035670000400829500080003060020005807800206005301700020040900070002487006000052490");
        insertSudoku(db,34, 2, "030070902470009000009003060024000837007000100351000620040900200000400056708050090");
        insertSudoku(db,35, 2, "084200000930840000057000000600401700400070002005602009000000980000028047000003210");
        insertSudoku(db,36, 2, "007861000008003000560090010100070085000345000630010007050020098000600500000537100");
        insertSudoku(db,37, 2, "040001003000050079560002804100270080082000960030018007306100098470080000800500040");
        insertSudoku(db,38, 2, "000500006000870302270300081000034900793050614008790000920003057506087000300005000");
        insertSudoku(db,39, 2, "000900067090000208460078000320094070700603002010780043000850016501000090670009000");
        insertSudoku(db,40, 2, "024000017000301000300000965201000650000637000093000708539000001000502000840000570");
        insertSudoku(db,41, 2, "200006143004000600607008029100800200003090800005003001830500902006000400942600005");
        insertSudoku(db,42, 2, "504002030900073008670000020000030780005709200047060000050000014100450009060300502");
        insertSudoku(db,43, 2, "580000637000000000603540000090104705010709040807205090000026304000000000468000072");
        insertSudoku(db,44, 2, "000010000900003408670500021000130780015000240047065000750006014102400009000090000");
        insertSudoku(db,45, 2, "780300050956000000002065001003400570600000003025008100200590800000000417030004025");
        insertSudoku(db,46, 2, "200367500500800060300450700090530400080000070003074050001026005030005007002783001");
        insertSudoku(db,47, 2, "801056200000002381900003000350470000008000100000068037000600002687100000004530806");
        insertSudoku(db,48, 2, "300004005841753060000010000003000087098107540750000100000070000030281796200300008");
        insertSudoku(db,49, 2, "000064810040050062009010300003040607008107500704030100006070200430080090017390000");
        insertSudoku(db,50, 2, "000040320000357080000600400357006040600705003080900675008009000090581000064070000");
        insertSudoku(db,51, 2, "905040026026050900030600050350000009009020800100000075010009030003080760560070108");
        insertSudoku(db,52, 2, "010403060030017400200000300070080004092354780500070030003000005008530040050906020");
        insertSudoku(db,53, 2, "605900100000100073071300005009010004046293510700040600200001730160002000008009401");
        insertSudoku(db,54, 2, "049060002800210490100040000000035084008102300630470000000080001084051006700020950");
        insertSudoku(db,55, 2, "067020300003700000920103000402035060300000002010240903000508039000009200008010750");
        insertSudoku(db,56, 2, "050842001004000900800050040600400019007506800430009002080090006001000400500681090");
        insertSudoku(db,57, 2, "000076189000002030009813000025000010083000590070000460000365200010700000536120000");
        insertSudoku(db,58, 2, "080000030400368000350409700000003650003000900078100000004201076000856009060000020");
        insertSudoku(db,59, 2, "000500748589000001700086900302010580000000000067050204004760002200000867876005000");
        insertSudoku(db,60, 2, "021009008000004031740100025000007086058000170160800000910008052230900000800300410");
        insertSudoku(db,61, 3, "600300100071620000805001000500870901009000600407069008000200807000086410008003002");
        insertSudoku(db,62, 3, "906013008058000090030000010060800920003409100049006030090000080010000670400960301");
        insertSudoku(db,63, 3, "300060250000500103005210486000380500030000040002045000413052700807004000056070004");
        insertSudoku(db,64, 3, "060001907100007230080000406018002004070040090900100780607000040051600009809300020");
        insertSudoku(db,65, 3, "600300208400185000000000450000070835030508020958010000069000000000631002304009006");
        insertSudoku(db,66, 3, "400030090200001600760800001500318000032000510000592008900003045001700006040020003");
        insertSudoku(db,67, 3, "004090170900070002007204000043000050798406213060000890000709400600040001085030700");
        insertSudoku(db,68, 3, "680001003007004000000820000870009204040302080203400096000036000000500400700200065");
        insertSudoku(db,69, 3, "000002000103400005200050401340005090807000304090300017605030009400008702000100000");
        insertSudoku(db,70, 3, "050702003073480005000050400040000200027090350006000010005030000400068730700109060");
        insertSudoku(db,71, 3, "500080020007502801002900040024000308000324000306000470090006700703208900060090005");
        insertSudoku(db,72, 3, "108090000200308096090000400406009030010205060080600201001000040360904007000060305");
        insertSudoku(db,73, 3, "010008570607050009052170000001003706070000040803700900000017260100020407024300090");
        insertSudoku(db,74, 3, "020439800080000001003001520050092703000000000309740080071300900600000030008924010");
        insertSudoku(db,75, 3, "000500201800006005005207080017960804000000000908074610080405300700600009504009000");
        insertSudoku(db,76, 3, "920000000500870000038091000052930160090000030073064980000410250000053001000000073");
        insertSudoku(db,77, 3, "590006010001254709000001400003715008100000004200648100002500000708463900050100047");
        insertSudoku(db,78, 3, "309870004000005008870400000104580003000706000700034105000009081900300000400057206");
        insertSudoku(db,79, 3, "800200000910300706000007002084000009095104860100000230500600000609003071000005008");
        insertSudoku(db,80, 3, "005037001000050627600002530020070000001968200000010090013700008486090000700840100");
        insertSudoku(db,81, 3, "090350700000800029000402008710000000463508297000000051300204000940005000008037040");
        insertSudoku(db,82, 3, "000005904080090605006000030030701450008040700074206090060000300801060070309800000");
        insertSudoku(db,83, 3, "030004087948700500060800009010586720000000000087312050800003070003007865570200090");
        insertSudoku(db,84, 3, "300687015000030082050000300400300000601050709000004003008000020210040000970521004");
        insertSudoku(db,85, 3, "702000004030702010400093008000827090007030800080956000300570009020309080600000503");
        insertSudoku(db,86, 3, "300040057400853060025700000000000430800406001034000000000005690090624003160080002");
        insertSudoku(db,87, 3, "000260050000005900000380046020094018004000500950810070380021000005700000040058000");
        insertSudoku(db,88, 3, "062080504008050090700320001000740620000203000027065000200036007040070100803090240");
        insertSudoku(db,89, 3, "002001000068000003000086090900002086804000102520800009080140000100000920000700500");
        insertSudoku(db,90, 3, "000030065460950200000086004003070006004090100500010300200140000007065028630020000");

        createIndexes(db);
    }

    private void insertSudoku(SQLiteDatabase db, long id, long lvId, String data) {
        String sql = "INSERT INTO Sudoku VALUES (" + id + ", " + lvId + ", " + data + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Sudoku");
        onCreate(db);
    }

    private void createIndexes(SQLiteDatabase db) {
        db.execSQL("create index sudoku_idx1 on Sudoku(id)");
    }
}
