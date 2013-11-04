package models;

import de.cm.osm2po.routing.RoutingResultSegment;

public class DirectedBlock {
    private final RoutingResultSegment segment;
    private final Block block;

    public DirectedBlock(RoutingResultSegment segment, Block block) {
        this.segment = segment;
        this.block = block;
    }

    public int getId() {
        return segment.getId();
    }

    public int getStreetId() {
        return block.getStreet().getId();
    }

    public int getSourceId() {
        return segment.getSourceId();
    }

    public int getTargetId() {
        return segment.getTargetId();
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getId());
        sb.append(": ");
        sb.append(getBlock().getStreet().getOsm_name());
        sb.append(" - ");
        sb.append(getBlock().getBars().size());
        sb.append(" bars");
        return sb.toString();
    }
}