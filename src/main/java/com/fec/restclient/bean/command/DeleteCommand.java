package com.fec.restclient.bean.command;

/*
 * Concrete command to delete files
 */
public class DeleteCommand implements Command{

    @Override
    public void execute() {
        System.out.println("Deleting file");
    }

}
