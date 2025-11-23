CGO_ENABLED=1 GOARCH=arm64 go build -o ../../../libhashing.dylib -buildmode=c-shared main.go
CGO_ENABLED=1 GOARCH=arm64 go build -o ../../../libhashing.dll -buildmode=c-shared main.go
CGO_ENABLED=1 GOARCH=arm64 go build -o ../../../libhashing.so -buildmode=c-shared main.go