use super::common::Direction;

pub struct Move {
    direction: Direction
}

pub enum Event {
    Move(Move)
}
