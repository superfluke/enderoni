package fluke.end.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import com.google.common.collect.Lists;

import fluke.end.block.ModBlocks;
import fluke.end.world.feature.WorldGenEndVolcano;
import fluke.end.world.feature.WorldGenEnderCanopy;

public class DebugCommand extends CommandBase 
{
	private final List<String> aliases;
	private static WorldGenEnderCanopy tree = new WorldGenEnderCanopy(false);
	private static WorldGenEndVolcano volc = new WorldGenEndVolcano(ModBlocks.endObsidian.getDefaultState(), ModBlocks.endMagma.getDefaultState(), Blocks.LAVA.getDefaultState());
	//private static WorldGenEndVolcano volc = new WorldGenEndVolcano(Blocks.LOG.getDefaultState(), ModBlocks.endMagma.getDefaultState(), Blocks.LAVA.getDefaultState());

	public DebugCommand()
	{
        aliases = Lists.newArrayList(Reference.MOD_ID, "debugDeco", "dd");
    }
	
	@Override
    @Nonnull
    public String getName() 
	{
        return "thisbiome";
    }
	
	@Override
    @Nonnull
    public List<String> getAliases() 
    {
        return aliases;
    }
	
	@Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        if (sender instanceof EntityPlayer) 
        {
        	double x = ((EntityPlayer) sender).posX;
        	double z = ((EntityPlayer) sender).posZ;
        	//tree.generate(server.getEntityWorld(), new Random(), new BlockPos(x+2, 4, z+2));
        	volc.generate(server.getEntityWorld(), new Random(), new BlockPos(x+2, 4, z+2));
        	//sender.sendMessage(new TextComponentString("hello"));
        }
        
    }
	
	@Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) 
	{
        return true;
    }
	
	@Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) 
    {
        return Collections.emptyList();
    }

	@Override
	public String getUsage(ICommandSender sender) 
	{
		return "debugDeco";
	}

}