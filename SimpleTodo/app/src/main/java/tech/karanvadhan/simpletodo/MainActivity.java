package tech.karanvadhan.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 789;
    // keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";


    //declaring variables
    ListView todolist;
    Button addButton;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapater;

    //when the android app was created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gets saved items #persistance
        readItems();
        //initializing new arrayadapter of type string
        itemsAdapater = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        //casting this because findViewById orginally takes an int but you need it to be a ListView
        todolist = (ListView) findViewById(R.id.todoList);
        //connecting the ListView to the adapter
        todolist.setAdapter(itemsAdapater);


        setupListViewListener();
    }

    public void onAddItem(View v)
    {
        EditText editTextNewItem = (EditText) findViewById(R.id.editText);
        //takes users input
        String itemText = editTextNewItem.getText().toString();
        //adds users text to the item adapter
        itemsAdapater.add(itemText);
        Log.i("MainActivity", "adding to do @" + items.size());
        //clears the editText so user can put in more inputs
        editTextNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(),"Item added to list",Toast.LENGTH_SHORT).show();
    }
    private void setupListViewListener()
    {
        Log.i("MainActivity", "setting the Long Click listner on the list view.");
        todolist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i("MainActivity", "item removed from the list:" + position);
                items.remove(position);
                itemsAdapater.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        todolist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //creating the new activity
                Intent intent = new Intent(MainActivity.this,editTodolist.class);
                // pass the data being edited
                intent.putExtra(ITEM_TEXT,items.get(position));
                //where the list needs to be updated.
                intent.putExtra(ITEM_POSITION,position);
                // display the activity
                startActivityForResult(intent,789);

            }
        });
    }


    //handles results from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit activity completed ok
        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE)
        {
            //extract updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item text at the edited position
            items.set(position, updatedItem);
            //notify the adapter that the model has changed
            itemsAdapater.notifyDataSetChanged();
            // persist the changed model
            writeItems();
            //notify the user the operation completed ok.
            Toast.makeText(this,"Item update successfully",Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile()
    {
        return new File(getFilesDir(),"todo.txt");
    }
    private void readItems()
    {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems()
    {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
