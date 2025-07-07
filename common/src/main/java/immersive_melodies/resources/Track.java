package immersive_melodies.resources;

import net.minecraft.network.FriendlyByteBuf;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Track {
    private final List<Note> notes;
    private final String name;

    public Track(String name, List<Note> notes) {
        this.name = name;
        this.notes = notes;
    }

    public Track(FriendlyByteBuf b) {
        name = b.readUtf();

        int noteCount = b.readInt();
        notes = new LinkedList<>();
        for (int i = 0; i < noteCount; i++) {
            notes.add(new Note(b));
        }
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public String getName() {
        return name;
    }

    public void encode(FriendlyByteBuf b) {
        b.writeUtf(name);

        b.writeInt(notes.size());
        for (Note note : notes) {
            note.encode(b);
        }
    }

    public int getLength() {
        if (notes.isEmpty()) return 0;
        Note note = notes.get(notes.size() - 1);
        return note.getTime() + note.getLength();
    }

    public void setNotes(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }
}
