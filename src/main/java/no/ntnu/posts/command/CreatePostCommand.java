package no.ntnu.posts.command;

import no.ntnu.command.Command;

public class CreatePostCommand implements Command {

    @Override
    public String getUsage() {
        return "<folder> <tag>";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void execute(String label, String[] args) {

    }
}
