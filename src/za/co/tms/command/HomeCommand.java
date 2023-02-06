package za.co.tms.command;

import java.io.IOException;

import javax.servlet.ServletException;

import org.bibeault.frontman.Command;
import org.bibeault.frontman.CommandContext;
import org.bibeault.frontman.FrontmanCommand;

@FrontmanCommand("home")
public class HomeCommand implements Command{

	public void execute(CommandContext commandContext) throws ServletException, IOException
	{		
		commandContext.forwardToView("home");
	}
}
