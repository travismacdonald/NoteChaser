# 69975636 == v0.46.1
curl -sSL https://api.github.com/repos/pinterest/ktlint/releases/69975636 \
    | grep "browser_download_url.*ktlint\"" \
    | cut -d : -f 2,3 \
    | tr -d \" \
    | wget -qi -\
    && chmod a+x ktlint \
    && mv ktlint /usr/local/bin/

ktlint --relative --color
