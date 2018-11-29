package tech.karanvadhan.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static tech.karanvadhan.simpletodo.MainActivity.ITEM_POSITION;
import static tech.karanvadhan.simpletodo.MainActivity.ITEM_TEXT;

public class editTodolist extends AppCompatActivity
{
    //track edit text
    EditText editText;
    //position of edited item in the list
    int position;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todolist);

        // resolving edit text from layout
        editText = (EditText) findViewById(R.id.editText2);
        // set edit text value from intent extra
        editText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION,0);
    }

    public void saveItem(View v)
    {
        // prepare new intent for result
        Intent intent = new Intent();
        // pass updated item text as extra
        intent.putExtra(ITEM_TEXT,editText.getText().toString());
        // pass original position as extra
        intent.putExtra(ITEM_POSITION,position);
        //set the intent as the result of the activity
        setResult(RESULT_OK, intent);
        // closing the activity and redirecting it to main activity
        finish();
    }
}
