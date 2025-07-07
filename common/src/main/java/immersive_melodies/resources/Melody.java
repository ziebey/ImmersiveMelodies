package immersive_melodies.resources;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;


public class Melody extends MelodyDescriptor {
    static final Melody DEFAULT = new Melody();

    private final List<Track> tracks = new LinkedList<>();

    public Melody() {
        super("unknown");
        addTrack(new Track("unknown", new LinkedList<>()));
    }

    public Melody(String name) {
        super(name);
    }

    public Melody(FriendlyByteBuf b) {
        super(b);

        int trackCount = b.readInt();
        for (int i = 0; i < trackCount; i++) {
            tracks.add(new Track(b));
        }
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public int getLength() {
        int length = 0;
        for (Track track : tracks) {
            length = Math.max(length, track.getLength());
        }
        return length;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public void encode(FriendlyByteBuf b) {
        super.encodeLite(b);

        b.writeInt(tracks.size());
        for (Track note : tracks) {
            note.encode(b);
        }
    }

    public void trim() {
        int offset = getFirstNoteTime();
        for (Track track : tracks) {
            List<Note> newNotes = new LinkedList<>();
            for (Note note : track.getNotes()) {
                newNotes.add(new Note(
                        note.getNote(),
                        note.getVelocity(),
                        note.getTime() - offset,
                        note.getLength(),
                        note.getSustain()
                ));
            }
            track.setNotes(newNotes);
        }
    }

    private int getFirstNoteTime() {
        int earliestNote = Integer.MAX_VALUE;
        for (Track track : tracks) {
            List<Note> notes = track.getNotes();
            if (!notes.isEmpty()) {
                earliestNote = Math.min(earliestNote, notes.get(0).getTime());
            }
        }
        return earliestNote;
    }
}
