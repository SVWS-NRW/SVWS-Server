import plugin from 'tailwindcss/plugin';

/**
 * Icons - Beispiel zur Verwendung in den vue-Dateien:
 *
 * <span class="icon icon-ui-brand i-ri-alert-line"/>
 */

const iconSize = {
	'xxs': '0.5rem',
	'xs': '0.75rem',
	'sm': '1rem',
	DEFAULT: '1.2rem',
	'lg': '1.5rem',
	'xl': '2rem',
	'xxl': '4rem',
}

const iconColors = {
	'white': 'invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);',
	'gray': 'invert(32%) sepia(97%) saturate(0%) hue-rotate(163deg) brightness(103%) contrast(104%);',
	'primary': 'invert(44%) sepia(52%) saturate(1260%) hue-rotate(173deg) brightness(91%) contrast(86%);',
	'light': 'invert(91%) sepia(3%) saturate(126%) hue-rotate(7deg) brightness(108%) contrast(94%);',
	'dark-20': 'invert(87%) sepia(7%) saturate(231%) hue-rotate(163deg) brightness(97%) contrast(87%);',
	'dark': 'invert(23%) sepia(18%) saturate(978%) hue-rotate(158deg) brightness(96%) contrast(91%);',
	'error': 'invert(22%) sepia(96%) saturate(2323%) hue-rotate(331deg) brightness(88%) contrast(103%);',
	'danger': 'invert(22%) sepia(96%) saturate(2323%) hue-rotate(331deg) brightness(88%) contrast(103%);',
	'warning': 'brightness(0) saturate(100%) invert(75%) sepia(17%) saturate(7311%) hue-rotate(11deg) brightness(97%) contrast(94%);',
	'caution': 'brightness(0) saturate(100%) invert(51%) sepia(100%) saturate(2298%) hue-rotate(355deg) brightness(101%) contrast(101%);',
	'success': 'invert(86%) sepia(40%) saturate(5221%) hue-rotate(37deg) brightness(101%) contrast(83%);',
	'highlight': 'invert(88%) sepia(25%) saturate(695%) hue-rotate(346deg) brightness(107%) contrast(106%);',
	'statistics': 'brightness(0) saturate(100%) invert(37%) sepia(71%) saturate(868%) hue-rotate(224deg) brightness(103%) contrast(93%);',
	'ui': 'none;',
	'ui-brand': 'brightness(0) saturate(100%) invert(37%) sepia(100%) saturate(422%) hue-rotate(172deg) brightness(93%) contrast(82%);',
	'ui-statistic': 'brightness(0) saturate(100%) invert(28%) sepia(56%) saturate(3209%) hue-rotate(255deg) brightness(96%) contrast(101%);',
	'ui-danger': 'brightness(0) saturate(100%) invert(26%) sepia(66%) saturate(2654%) hue-rotate(332deg) brightness(87%) contrast(85%);',
	'ui-success': 'brightness(0) saturate(100%) invert(36%) sepia(34%) saturate(945%) hue-rotate(55deg) brightness(110%) contrast(85%);',
	'ui-warning': 'brightness(0) saturate(100%) invert(56%) sepia(19%) saturate(1401%) hue-rotate(11deg) brightness(95%) contrast(87%);',
	'ui-caution': 'brightness(0) saturate(100%) invert(31%) sepia(43%) saturate(1936%) hue-rotate(347deg) brightness(101%) contrast(90%);',
	'ui-neutral': 'brightness(200%);',
	'ui-onbrand': 'invert(100%);',
	'ui-onstatistic': 'invert(100%);',
	'ui-onselected': 'brightness(0) saturate(100%) invert(21%) sepia(83%) saturate(516%) hue-rotate(174deg) brightness(97%) contrast(98%);',
	'ui-ondanger': 'invert(100%);',
	'ui-onsuccess': 'invert(100%);',
	'ui-onwarning': 'none;',
	'ui-oncaution': 'none;',
	'ui-onneutral': 'none;',
	'ui--dark': 'invert(100%);',
	'ui-brand--dark': 'brightness(0) saturate(100%) invert(74%) sepia(30%) saturate(6750%) hue-rotate(187deg) brightness(87%) contrast(88%);',
	'ui-statistic--dark': 'brightness(0) saturate(100%) invert(46%) sepia(48%) saturate(867%) hue-rotate(225deg) brightness(101%) contrast(94%);',
	'ui-danger--dark': 'brightness(0) saturate(100%) invert(52%) sepia(22%) saturate(1384%) hue-rotate(306deg) brightness(94%) contrast(96%);',
	'ui-success--dark': 'brightness(0) saturate(100%) invert(56%) sepia(63%) saturate(457%) hue-rotate(48deg) brightness(98%) contrast(90%);',
	'ui-warning--dark': 'brightness(0) saturate(100%) invert(52%) sepia(73%) saturate(390%) hue-rotate(11deg) brightness(90%) contrast(90%);',
	'ui-caution--dark': 'brightness(0) saturate(100%) invert(26%) sepia(72%) saturate(1684%) hue-rotate(354deg) brightness(102%) contrast(82%);',
	'ui-neutral--dark': 'invert(100%);',
	'ui-onbrand--dark': 'invert(100%);',
	'ui-onstatistic--dark': 'invert(100%);',
	'ui-onselected--dark': 'brightness(0) saturate(100%) invert(81%) sepia(8%) saturate(1290%) hue-rotate(180deg) brightness(98%) contrast(95%);',
	'ui-ondanger--dark': 'invert(100%);',
	'ui-onsuccess--dark': 'invert(100%);',
	'ui-onwarning--dark': 'none;',
	'ui-oncaution--dark': 'none;',
	'ui-onneutral--dark': 'invert(100%);',
};

const iconPath = {
	'add-line': { path: 'System', icon: 'add-line' }, // i-ri-add-line
	'add-circle-line': { path: 'System', icon: 'add-circle-line' }, // i-ri-add-circle-line
	'add-circle-fill': { path: 'System', icon: 'add-circle-fill' }, // i-ri-add-circle-fill
	'alert-fill': { path: 'System', icon: 'alert-fill' }, // i-ri-alert-fill
	'alert-line': { path: 'System', icon: 'alert-line' }, // i-ri-alert-line
	'archive-line': { path: 'Business', icon: 'archive-line' }, // i-ri-archive-line
	'archive-stack-line': { path: 'Business', icon: 'archive-stack-line' }, // i-ri-archive-stack-line
	'arrow-down-line': { path: 'Arrows', icon: 'arrow-down-line' }, // i-ri-arrow-down-line
	'arrow-down-s-line': { path: 'Arrows', icon: 'arrow-down-s-line' }, // i-ri-arrow-down-s-line
	'arrow-go-back-line': { path: 'Arrows', icon: 'arrow-go-back-line' }, // i-ri-arrow-go-back-line
	'arrow-left-s-line': { path: 'Arrows', icon: 'arrow-left-s-line' }, // i-ri-arrow-left-s-line
	'arrow-right-circle-line': { path: 'Arrows', icon: 'arrow-right-circle-line' }, // i-ri-arrow-right-circle-line
	'arrow-right-s-line': { path: 'Arrows', icon: 'arrow-right-s-line' }, // i-ri-arrow-right-s-line
	'arrow-turn-back-line': { path: 'Arrows', icon: 'arrow-turn-back-line' }, // i-ri-arrow-turn-back-line
	'arrow-up-down-line': { path: 'Arrows', icon: 'arrow-up-down-line' }, // i-ri-arrow-up-down-line
	'arrow-up-line': { path: 'Arrows', icon: 'arrow-up-line' }, // i-ri-arrow-up-line
	'arrow-up-s-line': { path: 'Arrows', icon: 'arrow-up-s-line' }, // i-ri-arrow-up-s-line
	'at-line': { path: 'Business', icon: 'at-line' }, // i-ri-at-line
	'bar-chart-2-line': { path: 'Business', icon: 'bar-chart-2-line' }, // i-ri-bar-chart-2-line
	'book-2-line': { path: 'Document', icon: 'book-2-line' }, // i-ri-book-2-line
	'box-3-line': { path: 'Others', icon: 'box-3-line' }, // i-ri-box-3-line
	'briefcase-line': { path: 'Business', icon: 'briefcase-line' }, // i-ri-briefcase-line
	'bug-fill': { path: 'Development', icon: 'bug-fill' }, // i-ri-bug-fill
	'calculator-line': { path: 'Business', icon: 'calculator-line' }, // i-ri-calculator-line
	'calendar-2-line': { path: 'Business', icon: 'calendar-2-line' }, // i-ri-calendar-2-line
	'calendar-event-line': { path: 'Business', icon: 'calendar-event-line' }, // i-ri-calendar-event-line
	'camera-fill': { path: 'Media', icon: 'camera-fill' }, // i-ri-camera-fill
	'camera-line': { path: 'Media', icon: 'camera-line' }, // i-ri-camera-line
	'camera-off-line': { path: 'Media', icon: 'camera-off-line' }, // i-ri-camera-off-line
	'chat-1-line': { path: 'Communication', icon: 'chat-1-line' }, // i-ri-chat-1-line
	'check-line': { path: 'System', icon: 'check-line' }, // i-ri-check-line
	'checkbox-blank-circle-line': { path: 'System', icon: 'checkbox-blank-circle-line' }, // i-ri-checkbox-blank-circle-line
	'checkbox-circle-fill': { path: 'System', icon: 'checkbox-circle-fill' }, // i-ri-checkbox-circle-fill
	'checkbox-circle-line': { path: 'System', icon: 'checkbox-circle-line' }, // i-ri-checkbox-circle-line
	'close-line': { path: 'System', icon: 'close-line' }, // i-ri-close-line
	'corner-right-up-line': { path: 'Arrows', icon: 'corner-right-up-line' }, // i-ri-corner-right-up-line
	'cup-line': { path: 'Food', icon: 'cup-line' }, // i-ri-cup-line
	'database-2-line': { path: 'Device', icon: 'database-2-line' }, // i-ri-database-2-line
	'delete-bin-fill': { path: 'System', icon: 'delete-bin-fill' }, // i-ri-delete-bin-fill
	'delete-bin-line': { path: 'System', icon: 'delete-bin-line' }, // i-ri-delete-bin-line
	'device-recover-line': { path: 'Device', icon: 'device-recover-line' }, // i-ri-device-recover-line
	'download-2-line': { path: 'System', icon: 'download-2-line' }, // i-ri-download-2-line
	'draft-line': { path: 'Document', icon: 'draft-line' }, // i-ri-draft-line
	'draggable': { path: 'Editor', icon: 'draggable' }, // i-ri-draggable
	'edit-2-line': { path: 'Design', icon: 'edit-2-line' }, // i-ri-edit-2-line
	'eraser-fill': { path: 'Design', icon: 'eraser-fill' }, // i-ri-eraser-fill
	'eraser-line': { path: 'Design', icon: 'eraser-line' }, // i-ri-eraser-line
	'error-warning-fill': { path: 'System', icon: 'error-warning-fill' }, // i-ri-error-warning-fill
	'error-warning-line': { path: 'System', icon: 'error-warning-line' }, // i-ri-error-warning-line
	'expand-height-line': { path: 'Arrows', icon: 'expand-height-line' }, // i-ri-expand-height-line
	'expand-width-line': { path: 'Arrows', icon: 'expand-width-line' }, // i-ri-expand-width-line
	'expand-right-line': { path: 'Arrows', icon: 'expand-right-line' }, // i-ri-expand-right-line
	'contract-left-line': { path: 'Arrows', icon: 'contract-left-line' }, // i-ri-contract-left-line
	'expand-up-down-fill': { path: 'Arrows', icon: 'expand-up-down-fill' }, // i-ri-expand-up-down-fill
	'expand-up-down-line': { path: 'Arrows', icon: 'expand-up-down-line' }, // i-ri-expand-up-down-line
	'eye-line': { path: 'System', icon: 'eye-line' }, // i-ri-eye-line
	'file-close-line': { path: 'Document', icon: 'file-close-line' }, // i-ri-file-close-line
	'file-copy-line': { path: 'Document', icon: 'file-copy-line' }, // i-ri-file-copy-line
	'file-damage-line': { path: 'Document', icon: 'file-damage-line' }, // i-ri-file-damage-line
	'filter-fill': { path: 'System', icon: 'filter-fill' }, // i-ri-filter-fill
	'filter-line': { path: 'System', icon: 'filter-line' }, // i-ri-filter-line
	'filter-off-line': { path: 'System', icon: 'filter-off-line' }, // i-ri-filter-off-line
	'folder-open-line': { path: 'Document', icon: 'folder-open-line' }, // i-ri-folder-open-line
	'forbid-fill': { path: 'System', icon: 'forbid-fill' }, // i-ri-forbid-fill
	'forbid-line': { path: 'System', icon: 'forbid-line' }, // i-ri-forbid-line
	'fullscreen-line': { path: 'Media', icon: 'fullscreen-line' }, // i-ri-fullscreen-line
	'graduation-cap-line': { path: 'Others', icon: 'graduation-cap-line' }, // i-ri-graduation-cap-line
	'group-line': { path: 'User%20&%20Faces', icon: 'group-line' }, // i-ri-group-line
	'headphone-line': { path: 'Media', icon: 'headphone-line' }, // i-ri-headphone-line
	'information-fill': { path: 'System', icon: 'information-fill' }, // i-ri-information-fill
	'information-line': { path: 'System', icon: 'information-line' }, // i-ri-information-line
	'lightbulb-line': { path: 'Others', icon: 'lightbulb-line' }, // i-ri-lightbulb-line
	'link': { path: 'Editor', icon: 'link' }, // i-ri-link
	'loader-4-line': { path: 'System', icon: 'loader-4-line' }, // i-ri-loader-4-line
	'lock-2-line': { path: 'System', icon: 'lock-2-line' }, // i-ri-lock-2-line
	'lock-fill': { path: 'System', icon: 'lock-fill' }, // i-ri-lock-fill
	'lock-line': { path: 'System', icon: 'lock-line' }, // i-ri-lock-line
	'lock-unlock-line': { path: 'System', icon: 'lock-unlock-line' }, // i-ri-lock-unlock-line
	'login-circle-line': { path: 'System', icon: 'login-circle-line' }, // i-ri-login-circle-line
	'logout-circle-line': { path: 'System', icon: 'logout-circle-line' }, // i-ri-logout-circle-line
	'loop-left-line': { path: 'System', icon: 'loop-left-line' }, // i-ri-loop-left-line
	'loop-right-line': { path: 'System', icon: 'loop-right-line' }, // i-ri-loop-right-line
	'mail-send-line': { path: 'Business', icon: 'mail-send-line' }, // i-ri-mail-send-line
	'menu-fold-line': { path: 'System', icon: 'menu-fold-line' }, // i-ri-menu-fold-line
	'menu-line': { path: 'System', icon: 'menu-line' }, // i-ri-menu-line
	'menu-unfold-line': { path: 'System', icon: 'menu-unfold-line' }, // i-ri-menu-unfold-line
	'moon-line': { path: 'Weather', icon: 'moon-line' }, // i-ri-moon-line
	'palette-line': { path: 'Design', icon: 'palette-line' }, // i-ri-palette-line
	'phone-line': { path: 'Device', icon: 'phone-line' }, // i-ri-phone-line
	'presentation-line': { path: 'Business', icon: 'presentation-line' }, // i-ri-presentation-line
	'printer-line': { path: 'Business', icon: 'printer-line' }, // i-ri-printer-line
	'prohibited-fill': { path: 'System', icon: 'prohibited-fill' }, // i-ri-prohibited-fill
	'prohibited-line': { path: 'System', icon: 'prohibited-line' }, // i-ri-prohibited-line
	'pushpin-fill': { path: 'Map', icon: 'pushpin-fill' }, // i-ri-pushpin-fill
	'pushpin-line': { path: 'Map', icon: 'pushpin-line' }, // i-ri-pushpin-line
	'question-line': { path: 'System', icon: 'question-line' }, // i-ri-question-line
	'question-fill': { path: 'System', icon: 'question-fill' }, // i-ri-question-fill
	'refresh-line': { path: 'System', icon: 'refresh-line' }, // i-ri-refresh-line
	'restart-line': { path: 'Device', icon: 'restart-line' }, // i-ri-restart-line
	'save-3-line': { path: 'Device', icon: 'save-3-line' }, // i-ri-save-3-line
	'school-line': { path: 'Buildings', icon: 'school-line' }, // i-ri-school-line
	'search-line': { path: 'System', icon: 'search-line' }, // i-ri-search-line
	'send-plane-fill': { path: 'Business', icon: 'send-plane-fill' }, // i-ri-send-plane-fill
	'settings-2-line': { path: 'System', icon: 'settings-2-line' }, // i-ri-settings-2-line
	'settings-3-line': { path: 'System', icon: 'settings-3-line' }, // i-ri-settings-3-line
	'share-forward-2-line': { path: 'System', icon: 'share-forward-2-line' }, // i-ri-share-forward-2-line
	'share-forward-line': { path: 'System', icon: 'share-forward-line' }, // i-ri-share-forward-line
	'shield-star-line': { path: 'System', icon: 'shield-star-line' }, // i-ri-shield-star-line
	'spam-3-line': { path: 'System', icon: 'spam-3-line' }, // i-ri-spam-3-line
	'sparkling-line': { path: 'Weather', icon: 'sparkling-line' }, // i-ri-sparkling-line
	'speak-line': { path: 'Communication', icon: 'speak-line' }, // i-ri-speak-line
	'speed-line': { path: 'Media', icon: 'speed-line' }, // i-ri-speed-line
	'subtract-line': { path: 'System', icon: 'subtract-line' }, // i-ri-subtract-line
	'sun-line': { path: 'Weather', icon: 'sun-line' }, // i-ri-sun-line
	'table-line': { path: 'Design', icon: 'table-line' }, // i-ri-table-line
	'team-line': { path: 'User%20&%20Faces', icon: 'team-line' }, // i-ri-team-line
	'text': { path: 'Editor', icon: 'text' }, // i-ri-text
	'time-line': { path: 'System', icon: 'time-line' }, // i-ri-time-line
	'upload-2-line': { path: 'System', icon: 'upload-2-line' }, // i-ri-upload-2-line
	'user-add-line': { path: 'User%20&%20Faces', icon: 'user-add-line' }, // i-ri-user-add-line
	'user-forbid-line': { path: 'User%20&%20Faces', icon: 'user-forbid-line' }, // i-ri-user-forbid-line
	'vidicon-line': { path: 'Media', icon: 'vidicon-line' }, // i-ri-vidicon-line
	'zoom-in-line': { path: 'System', icon: 'zoom-in-line' }, // i-ri-zoom-in-line
	'zoom-out-line': { path: 'System', icon: 'zoom-out-line' }, // i-ri-zoom-out-line
	'play-line': { path: 'Media', icon: 'play-line' }, // i-ri-play-line
	'message-line': { path: 'Communication', icon: 'message-line' }, // i-ri-message-line
	'chat-new-line': { path: 'Communication', icon: 'chat-new-line' }, // i-ri-chat-new-line
	'question-mark': { path: 'Editor', icon: 'question-mark' }, // i-ri-question-mark
	'asterisk': { path: 'Editor', icon: 'asterisk' }, // i-ri-asterisk
	'layout-column-line': { path: 'Design', icon: 'layout-column-line' }, // ri-layout-column-line
	'layout-column-fill': { path: 'Design', icon: 'layout-column-fill' }, // ri-layout-column-fill
}

const iconPlugins = [
	// eslint-disable-next-line @typescript-eslint/unbound-method
	plugin(function({ matchUtilities, theme }) {
		matchUtilities({
			'i-ri': (value) => ({
				'background-image': `url(remixicon/icons/${value.path}/${value.icon}.svg)`,
				'background-repeat': 'no-repeat',
				'background-position': 'center',
			}),
		}, { values: theme('iconPath') })
	}),
	// eslint-disable-next-line @typescript-eslint/unbound-method
	plugin(function({ matchUtilities, theme }) {
		matchUtilities({
			'icon': (value) => ({
				'-webkit-filter': value,
				'filter': value,
			}),
		}, { values: theme('iconColors') })
	}),
	// eslint-disable-next-line @typescript-eslint/unbound-method
	plugin(function({ matchUtilities, theme }) {
		matchUtilities({
			'icon': (value) => ({
				'width': value,
				'height': value,
				'line-height': value,
			}),
		}, { values: theme('iconSize') })
	}),
];

export { iconSize, iconColors, iconPath, iconPlugins };
