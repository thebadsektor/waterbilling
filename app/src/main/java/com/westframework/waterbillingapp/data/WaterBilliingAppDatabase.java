package com.westframework.waterbillingapp.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.westframework.waterbillingapp.data.amnesty.Amnesty;
import com.westframework.waterbillingapp.data.amnesty.AmnestyDao;
import com.westframework.waterbillingapp.data.application.Application;
import com.westframework.waterbillingapp.data.application.ApplicationDao;
import com.westframework.waterbillingapp.data.bill.WaterBill;
import com.westframework.waterbillingapp.data.bill.WaterBillDao;
import com.westframework.waterbillingapp.data.classification.Classification;
import com.westframework.waterbillingapp.data.classification.ClassificationDao;
import com.westframework.waterbillingapp.data.discount.Discount;
import com.westframework.waterbillingapp.data.discount.DiscountDao;
import com.westframework.waterbillingapp.data.holiday.Holiday;
import com.westframework.waterbillingapp.data.holiday.HolidayDao;

import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = {
        WaterBill.class,
        Application.class,
        Classification.class,
        Holiday.class,
        Discount.class,
        Amnesty.class
        }, version = 43, exportSchema = false)
public abstract class WaterBilliingAppDatabase extends RoomDatabase {

    private static WaterBilliingAppDatabase instance;

    public abstract WaterBillDao waterBillDao();
    public abstract ApplicationDao applicationDao();
    public abstract ClassificationDao classificationDao();
    public abstract DiscountDao discountDao();
    public abstract HolidayDao holidayDao();
    public abstract AmnestyDao amnestyDao();

    public static synchronized WaterBilliingAppDatabase getInstance(android.app.Application context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WaterBilliingAppDatabase.class, "water_billing_app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private final WaterBillDao waterBillDao;
        private final ApplicationDao applicationDao;
        private final ClassificationDao classificationDao;
        private final DiscountDao discountDao;
        private final HolidayDao holidayDao;
        private final AmnestyDao amnestyDao;

        private PopulateDbAsyncTask(WaterBilliingAppDatabase database){
            waterBillDao = database.waterBillDao();
            applicationDao = database.applicationDao();
            classificationDao = database.classificationDao();
            discountDao = database.discountDao();
            holidayDao = database.holidayDao();
            amnestyDao = database.amnestyDao();
        }

        @Override
        protected  Void doInBackground(Void... voids){

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            String timeStampS = s.format(new Date());
            String timeStampD = d.format(new Date());

            waterBillDao.Insert(new WaterBill(
                    "2013-010-057",
                    "8-4-2022",
                    "11-5-2022",
                    "Salve Miras",
                    "Layug",
                    "Residential",
                    318,
                    "987-987",
                    "August 2022",
                    1942,
                    0,
                    48,
                    0.2,
                    0,
                    0.2,
                    50,
                    "unpaid",
                    "2013-010-057-318-000",
                    0,
                    357,
                    0,
                    "",
                    "WS Bill",
                    "Active",
                    86,
                    35,
                    timeStampD,
                    timeStampD));
            waterBillDao.Insert(new WaterBill(
                    "2013-010-057",
                    "8-4-2022",
                    "8-19-2022",
                    "Sion Blaza",
                    "Layug",
                    "Residential",
                    318,
                    "987-987",
                    "August 2022",
                    1942,
                    0,
                    48,
                    0.2,
                    0,
                    0.2,
                    100,
                    "Unpaid",
                    "2013-010-057-318-001",
                    0,
                    951,
                    5,
                    "",
                    "WS Bill",
                    "Active",
                    86,
                    35,
                    timeStampD,
                    timeStampD));
            waterBillDao.Insert(new WaterBill(
                    "2013-010-057",
                    "7-28-2022",
                    "8-19-2022",
                    "Minda Valente",
                    "Layug",
                    "Residential",
                    318,
                    "987-987",
                    "August 2022",
                    1942,
                    0,
                    48,
                    0.2,
                    0,
                    0.2,
                    100,
                    "Paid",
                    "2013-010-057-318-002",
                    0,
                    789,
                    5,
                    "",
                    "WS Bill",
                    "Active",
                    86,
                    35,
                    timeStampD,
                    timeStampD));
            waterBillDao.Insert(new WaterBill(
                    "2022-004-3299",
                    "7-28-2022",
                    "11-6-2022",
                    "Minda Valente",
                    "Layug",
                    "Residential",
                    318,
                    "987-987",
                    "August 2022",
                    1942,
                    1990,
                    48,
                    0.2,
                    0,
                    0.2,
                    500,
                    "unpaid",
                    "2022-004-3299-318-003",
                    50,
                    263,
                    5,
                    "",
                    "WS Bill",
                    "Active",
                    86,
                    35,
                    timeStampD,
                    timeStampD));
            waterBillDao.Insert(new WaterBill(
                    "8526-951-357",
                    "7-28-2022",
                    "11-2-2022",
                    "Minda Valente",
                    "Layug",
                    "Residential",
                    318,
                    "987-987",
                    "August 2022",
                    1942,
                    1990,
                    48,
                    0.2,
                    0,
                    0.2,
                    500,
                    "unpaid",
                    "8526-951-357-318-004",
                    50,
                    150,
                    5,
                    "",
                    "WS Bill",
                    "Active",
                    86,
                    35,
                    timeStampD,
                    timeStampD));
            applicationDao.Insert(new Application(
                    "2013-010-057",
                    "04-06-1987",
                    "Villaran",
                    "M",
                    "Gerald",
                    "Residential",
                    "354",
                    "123",
                    "Sitio 5",
                    "Bagumbayan",
                    "987",
                    "Paid",
                    "Active",
                    timeStampS,
                    timeStampS));
            applicationDao.Insert(new Application(
                    "2000-004-006",
                    "08-06-2000",
                    "Lesbianini",
                    "Q",
                    "Beki Mae Luisito",
                    "Residential",
                    "MMC 354",
                    "Sitio 7",
                    "Sesame",
                    "Calios",
                    "987",
                    "Paid",
                    "Active",
                    timeStampS,
                    timeStampS));
            applicationDao.Insert(new Application(
                    "8526-951-357",
                    "05-08-1999",
                    "Smith",
                    "D.",
                    "John",
                    "Government",
                    "GG66",
                    "Building 1",
                    "Emp St.",
                    "Sto. Anghel",
                    "489",
                    "Paid",
                    "Active",
                    timeStampS,
                    timeStampS));
            applicationDao.Insert(new Application(
                    "2022-004-3299",
                    "05-08-1999",
                    "Smith",
                    "D.",
                    "John",
                    "Government",
                    "GG66",
                    "Building 1",
                    "Emp St.",
                    "Sto. Anghel",
                    "489",
                    "Paid",
                    "Active",
                    timeStampS,
                    timeStampS));

            classificationDao.Insert(new Classification("Residential", 1, 3, 50, 0, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Residential", 4, 5, 75, 0, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Residential", 6, 10, 100, 0, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Residential", 11, 20, 100, 10, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Residential", 21, 30, 200, 12, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Residential", 31, 3000, 320, 14, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Residential", 3001, 9999, 320, 14, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Commercial", 1, 10, 150, 0, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Commercial", 11, 20, 150, 15, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Commercial", 21, 30, 317, 17, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Commercial", 31, 3000, 470, 20, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Commercial", 3001, 9999, 470, 20, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Water Refilling", 1, 10, 250, 0, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Water Refilling", 11, 20, 250, 15, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Water Refilling", 21, 30, 250, 17, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Water Refilling", 31, 5000, 250, 20, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Water Refilling", 5001, 9999, 250, 20, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Government", 1, 10, 150, 0, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Government", 11, 20, 165, 17, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Government", 21, 30, 317, 17, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Government", 31, 5000, 490, 20, timeStampS, timeStampS));
            classificationDao.Insert(new Classification("Government", 5001, 9999, 490, 20, timeStampS, timeStampS));

            discountDao.Insert(new Discount("Penalty", "Percent", "2% Penalty After Due date", 0.02));
            discountDao.Insert(new Discount("Discount", "Percent", "5% Discount for Senior Citizen", 0.05));
            discountDao.Insert(new Discount("Discount", "Amount", "Courtesy Discount 1", 50));

            holidayDao.Insert(new Holiday(
                    "New Year",
                    "January",
                    "1",
                    "2022",
                    timeStampS,
                    timeStampS));

            holidayDao.Insert(new Holiday(
                    "New Year",
                    "December",
                    "25",
                    "2022",
                    timeStampS,
                    timeStampS));

            holidayDao.Insert(new Holiday(
                    "Labor day",
                    "May",
                    "1",
                    "2022",
                    timeStampS,
                    timeStampS));

            holidayDao.Insert(new Holiday(
                    "Labor day",
                    "November",
                    "2",
                    "2022",
                    timeStampS,
                    timeStampS));

            holidayDao.Insert(new Holiday(
                    "Labor day",
                    "November",
                    "3",
                    "2022",
                    timeStampS,
                    timeStampS));

            holidayDao.Insert(new Holiday(
                    "Labor day",
                    "November",
                    "4",
                    "2022",
                    timeStampS,
                    timeStampS));

            amnestyDao.Insert(new Amnesty(
                    "Covid",
                    "August",
                    "Covid2",
                    "2022",
                    timeStampS,
                    timeStampS
            ));

            amnestyDao.Insert(new Amnesty(
                    "Covidoo",
                    "August",
                    "Covid2",
                    "2022",
                    timeStampS,
                    timeStampS
            ));

            amnestyDao.Insert(new Amnesty(
                    "Covid",
                    "August",
                    "Covid2",
                    "2022",
                    timeStampS,
                    timeStampS
            ));

            amnestyDao.Insert(new Amnesty(
                    "Covid",
                    "April",
                    "Covid2",
                    "1987",
                    timeStampS,
                    timeStampS
            ));

            return null;
        }
    }
}
