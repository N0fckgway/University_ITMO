package code.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

/*
 * X-Scissor наносит урон без дополнительного эффекта.
 */


public final class XScissor extends PhysicalMove {
    public XScissor() {
        super(Type.BUG, 80, 100);
    }

    @Override
    protected String describe() {
        return "Аккуратненько на кондициях юзнул XScissor";
    }
}
