package bzh.strawberry.survie.loterie.commands;

import bzh.strawberry.api.command.AbstractCommand;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * This file (LoterieCommand) is part of a project Survie.
 * It was created on 17/07/2020 18:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class LoterieCommand extends AbstractCommand {

    public LoterieCommand() {
        super(Survie.SURVIE, "survie.player.loterie");
    }

    @Override
    protected boolean onCommand(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length > 0) {
            if (strings[0].equals("info")) {
                player.sendMessage("§eLa cagnotte s'élève actuellement à §6" + new CurrencyFormat().format(Survie.SURVIE.getLotterieManager().getCagnotte()) + " Ecu");
                return true;
            } else if (strings[0].equals("tickets")) {
                player.sendMessage("§eVous avez actuellement §6" + Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) + " participation(s)");
                return true;
            } else if (strings[0].equals("tirage") && player.isOp()) {
                player.sendMessage("§eLancement du tirage de la loterie...");
                Survie.SURVIE.getLotterieManager().generateResult();
                return true;
            } else {
                player.sendMessage("§6§m----------------------------");
                player.sendMessage("§e/" + s + " info §8» §7Connaître la valeur de la cagnotte");
                player.sendMessage("§e/" + s + " §8» §7Acheter un ticket");
                player.sendMessage("§e/" + s + " tickets §8» §7Votre nombre de participation");
                if (player.isOp()) {
                    player.sendMessage("§e/" + s + " tirage §8» §7Lance de force le tirage de la loterie");
                }
                player.sendMessage("§6§m----------------------------");
                return false;
            }
        } else {

        }

        return true;
    }
}
