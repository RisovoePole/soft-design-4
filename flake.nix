{
  description = "Java development environment with jdtls and Gradle";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs =
    {
      self,
      nixpkgs,
      flake-utils,
    }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = import nixpkgs { inherit system; };
        jdk = pkgs.jdk21;
        gradle = pkgs.gradle.override { java = jdk; };
        jdtls = pkgs.jdt-language-server.override { inherit jdk; };
      in
      {
        devShells.default = pkgs.mkShell {
          packages = [
            jdk
            gradle
            jdtls
            pkgs.just
          ];

          JAVA_HOME = "${jdk}/lib/openjdk";
          # JFX_LIBS = pkgs.lib.makeLibraryPath (
          # with pkgs;
          # [
          # gtk3
          # glib
          #     pango
          #     cairo
          #     gdk-pixbuf
          #     atk
          #     freetype
          #     fontconfig
          # libGL
          #     libX11
          # libXtst
          # libXxf86vm
          #     libXext
          #     libXrender
          #     libXi
          #   ]
          # );
          shellHook = ''
            echo "Java dev shell loaded: $(java -version 2>&1 | head -n 1)"
          '';
        };
      }
    );
}
