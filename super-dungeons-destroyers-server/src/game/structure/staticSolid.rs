use super::{ Location };

pub enum SolidType {
    WALL,
    DOOR,
    LADDER,
}
pub struct StaticSolid {
    pub location: Option<Location>,
    pub solidType: SolidType 
}

impl StaticSolid {
    pub fn new() -> Self {
        StaticSolid {
            location: None,
            solidType: SolidType::WALL
        }
    }

}

