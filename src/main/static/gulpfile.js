var gulp = require('gulp');
var ts = require('gulp-typescript');
var tslint = require('gulp-tslint');
var sourcemaps = require('gulp-sourcemaps');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var del = require('del');
var gulpsync = require('gulp-sync')(gulp);
var jade = require('gulp-jade');
var sass = require('gulp-sass');
var es = require('event-stream');
var autoprefixer = require('gulp-autoprefixer');
var KarmaServer = require('karma').Server;
var angularTemplateCache = require('gulp-angular-templatecache');

gulp.task('css', gulpsync.sync(['images', 'css-build', 'css-third-party', 'css-third-party-resources']));
gulp.task('images', function () {
    return gulp.src('./assets/**/*.{gif,jpg,png,svg}')
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});
gulp.task('css-build', gulpsync.sync(['sass', 'autoprefixer', 'css-concat']));
gulp.task('css-third-party', function () {
    return gulp.src([
        './bower_components/open-sans/css/open-sans.min.css',
        './bower_components/normalize-css/normalize.css',
        './bower_components/font-awesome/css/font-awesome.min.css'
    ]).pipe(concat('_.css')).pipe(gulp.dest('../../../target/static-resources/app/third-party/styles/'));
});
gulp.task('css-third-party-resources', function () {
    var fonts = gulp.src([
        './bower_components/open-sans/fonts/**/*',
        './bower_components/font-awesome/fonts/*'
    ]).pipe(gulp.dest('../../../target/static-resources/app/third-party/fonts'));
    return es.concat(fonts);
});
gulp.task('sass', function () {
    return gulp.src('./assets/**/*.sass')
        .pipe(sass({
            outputStyle: 'compressed'
        }).on('error', sass.logError))
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});
gulp.task('autoprefixer', function () {
    return gulp.src('../../../target/static-resources/app/styles/app.css')
        .pipe(autoprefixer({}))
        .pipe(gulp.dest('../../../target/static-resources/app/styles/'));
});
gulp.task('css-concat', function () {
    return gulp.src(['../../../target/static-resources/app/styles/sprite.css', '../../../target/static-resources/app/styles/app.css'])
        .pipe(concat('_.css'))
        .pipe(gulp.dest('../../../target/static-resources/app/styles/'))
});

gulp.task('html', gulpsync.sync(['jade', 'html-to-js', 'copy-templates']));
gulp.task('jade', function () {
    return gulp.src('./assets/**/*.jade')
        .pipe(jade({
            locals: {}
        }))
        .pipe(gulp.dest('../../../target/static-templates/html'))
});
gulp.task('html-to-js', function () {
    return gulp.src('../../../target/static-templates/html/*/*.html')
        .pipe(angularTemplateCache({
            filename: '_templates.js',
            root: 'app/',
            module: 'javaeeio-main'
        }))
        .pipe(gulp.dest('../../../target/static-templates/'))
});
gulp.task('copy-templates', function () {
    return gulp.src('../../../target/static-templates/_templates.js')
        .pipe(gulp.dest('../../../target/static-resources/app/scripts/'));
});

gulp.task('js', gulpsync.sync(['lint-ts', 'compile-ts', 'copy-ts', 'js-third-party']));
gulp.task('lint-ts', function () {
    return gulp.src('./assets/**/*.ts')
        .pipe(tslint())
        .pipe(tslint.report('prose'));
});
gulp.task('compile-ts', function () {
    return gulp.src('./assets/**/*.ts')
        .pipe(sourcemaps.init())
        .pipe(ts({
            'target': 'es5',
            'sourceMap': true,
            'out': '_.js'
        }))
        .pipe(uglify({
            mangle: false // otherwhise the sourcemap/debugger does not work properly.
        }))
        .pipe(sourcemaps.write({includeContent: false}))
        .pipe(gulp.dest('../../../target/static-resources/app/scripts/'));
});
gulp.task('copy-ts', function () {
    return gulp.src('./assets/**/*.ts')
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});

gulp.task('js-third-party', function () {
    return gulp.src([
        './bower_components/underscore/underscore-min.js',
        './bower_components/jquery/dist/jquery.min.js',
        './bower_components/angular/angular.min.js',
        './bower_components/angular-route/angular-route.min.js',
        './bower_components/ngstorage/ngStorage.min.js',
        './bower_components/angular-cookies/angular-cookies.min.js',
        './bower_components/angular-resource/angular-resource.min.js',
        './node_modules/underscore.string/dist/underscore.string.min.js'
    ]).pipe(concat('_.js')).pipe(gulp.dest('../../../target/static-resources/app/third-party/'));
});

gulp.task('test', function (done) {
    new KarmaServer({
        configFile: __dirname + '/karma.conf.js'
    }, done).start();
});

gulp.task('copy-all', function () {
    return gulp.src(['../../../target/static-resources/**/*'])
        .pipe(gulp.dest('../../../target/apache-tomee/webapps/javaee-io/'));
});

gulp.task('clean', function (callback) {
    return del([
        '../../../target/static-resources/',
        '../../../target/apache-tomee/webapps/javaee-io/app/',
        '../../../target/apache-tomee/webapps/javaee-io/components/'
    ], {
        force: true
    }, callback);
});

gulp.task('build', gulpsync.sync(['clean', 'html', 'js', 'css', 'copy-all']));
gulp.task('build-with-tests', gulpsync.sync(['build', 'test']));

gulp.task('default', gulpsync.sync(['build']), function () {
    gulp.watch(
        ['./assets/**/*', '../../test/**/*.js'],
        gulpsync.sync(['build'])
    );
});
