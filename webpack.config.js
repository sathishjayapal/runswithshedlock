const path = require('path');
const TerserPlugin = require('terser-webpack-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const WarningsToErrorsPlugin = require('warnings-to-errors-webpack-plugin');


module.exports = (env, argv) => ({
  entry: {
    bundle: 'ts/app.ts'
  },
  output: {
    path: path.resolve(__dirname, './target/classes/static'),
    filename: 'js/[name].js'
  },
  devtool: argv.mode === 'production' ? false : 'eval-source-map',
  optimization: {
    minimize: true,
    minimizer: [
      new TerserPlugin(),
      new CssMinimizerPlugin()
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: 'css/[name].css'
    }),
    new WarningsToErrorsPlugin()
  ],
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: ['ts-loader']
      },
      {
        test: /\.css$/,
        use: [
          argv.mode === 'production' ? MiniCssExtractPlugin.loader : 'style-loader',
          {
            loader: 'css-loader',
            options: {
              importLoaders: 1
            }
          },
          {
            loader: 'postcss-loader',
            options: {
              postcssOptions: {
                plugins: [
                  require('@tailwindcss/postcss')
                ]
              }
            }
          }
        ]
      }
    ]
  },
  resolve: {
    plugins: [new TsconfigPathsPlugin({})],
    extensions: ['.js', '.jsx', '.ts', '.tsx', '.json']
  },
  devServer: {
    port: 8081,
    compress: true,
    hot: true,
    static: false,
    watchFiles: [
      'src/main/resources/templates/**/*.html',
      'src/main/resources/ts/**/*.ts',
      'src/main/resources/css/**/*.css'
    ],
    proxy: [
      {
        context: '**',
        target: 'http://localhost:8085',
        secure: false,
        prependPath: false,
        headers: {
          'X-Devserver': '1',
        }
      }
    ]
  }
});
