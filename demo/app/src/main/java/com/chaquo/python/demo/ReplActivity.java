package com.chaquo.python.demo;

import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import com.chaquo.python.*;


public class ReplActivity extends ConsoleActivity {

    private EditText etInput;

    protected static class State extends ConsoleActivity.State {
        PyObject console;
        boolean more = false;

        public State(ReplActivity activity) {
            PyObject locals = activity.py.getBuiltins().callAttr("dict", new Kwarg("context", activity));
            console = activity.py.getModule("code").callAttr("InteractiveConsole", locals);
        }
    }
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_repl);
        super.onCreate(savedInstanceState);
        state = (State) ((ConsoleActivity)this).state;

        etInput = (EditText) findViewById(R.id.etInput);
        etInput.setHint(getPrompt());
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String input = etInput.getText().toString();
                    etInput.setText("");
                    push(input);
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            PyObject sys = py.getModule("sys");
            append(String.format("Python %s on %s\n",sys.get("version"), sys.get("platform")));
            append(getString(R.string.repl_banner) + "\n");
            push("from java import *");
        }
    }

    @Override
    protected State initState() {
        return new State(this);
    }

    private void push(String input) {
        append(getPrompt() + input + "\n");
        state.more = state.console.callAttr("push", input).toJava(Boolean.class);
        etInput.setHint(getPrompt());
    }

    private String getPrompt() {
        return getString(state.more ? R.string.ps2 : R.string.ps1);
    }

    protected void scroll(int direction) {
        super.scroll(direction);
        etInput.requestFocus();
    }

}
