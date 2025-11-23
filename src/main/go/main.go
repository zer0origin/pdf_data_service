package main

import "C"
import "github.com/zer0origin/golang-image-hashing/hashing"

func main() {}

//export hashPageOfDocumentString
func hashPageOfDocumentString(cStr *C.char) *C.char {
	HashStruct := hashing.NewImageHash()
	k := convertFNVString(cStr, HashStruct)
	hash := HashStruct.Hash64shift(k)
	return convertCString(HashStruct.ConvertHashToString(hash))
}

func convertCString(cStr string) *C.char {
	return C.CString(cStr)
}

func convertFNVString(cStr *C.char, hash hashing.Hash) uint64 {
	data := C.GoString(cStr)
	return hash.ConvertByteArrToNumberFNV1ABase64Encoded(data)
}

// https://github.com/vladimirvivien/go-cshared-examples
