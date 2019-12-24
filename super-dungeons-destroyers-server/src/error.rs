use failure::Fail;

#[derive(Debug, Fail)]
#[fail(display = "option must be some but was none")]
pub struct NoneError;

#[derive(Debug, Fail)]
#[fail(display = "union must be something other than NONE")]
pub struct UnionNoneError;

#[derive(Debug, Fail)]
#[fail(display = "Buffer received is corrupted")]
pub struct CorruptedError;
