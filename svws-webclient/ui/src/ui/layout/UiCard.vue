<template>
	<div class="ui-card" :class="{ 'ui-card--compact' : compact }" tabindex="-1" @mouseover="isHovered = true" @mouseleave="isHovered = false">
		<!-- Header Section -->
		<component :is="collapsible ? 'button' : 'div'" :type="collapsible ? 'button' : 'text'" tabindex="0"
			:class="[{ 'ui-card--active': isActive }, headerBackgroundColor, headerTextColor, borderColor]"
			class="ui-card--header relative z-50 focus:ring-2 focus:ring-ui focus:!rounded outline-none"
			@click="collapsible ? setActive() : null" :aria-expanded="ariaExpanded" :aria-controls="collapsible ? 'cardBody' + instanceId : undefined">
			<!-- Left Collapse Icon -->
			<div v-if="showCollapseIconLeft" class="ui-card--header--collapse-icon">
				<transition name="ui-card--icon" mode="out-in">
					<!-- Closed Icon -->
					<span v-if="!isActive" :class="[collapseIconClosed, headerIconColor]" aria-label="Card geschlossen" />
					<!-- Opened Icon -->
					<span v-else :class="[collapseIconOpened, headerIconColor]" aria-label="Card geöffnet" />
				</transition>
			</div>
			<slot name="collapseLeft" />

			<!-- Icon Section -->
			<div v-if="showIcon" class="ui-card--header--icon"> <span :class="[icon, headerIconColor]" /> </div>
			<slot name="icon" />

			<!-- Title and Subtitle Section -->
			<div v-if="showTitleWrapper" class="ui-card--header--title-wrapper">
				<div v-if="showTitle" class="ui-card--header--title"> {{ title }} </div>
				<slot name="title" />
				<div v-if="showSubtitle" class="ui-card--header--subtitle"> {{ subtitle }} </div>
				<slot name="subtitle" />
			</div>

			<!-- Right Section -->
			<div class="ui-card--header--right-section">
				<!-- Info Section -->
				<div v-if="showInfo" class="ui-card--header--info"> {{ info }} </div>
				<slot name="info" />

				<!-- Right Collapse Icon -->
				<div v-if="showCollapseIconRight" class="ui-card--header--collapse-icon">
					<transition name="ui-card--icon" mode="out-in">
						<!-- Closed Icon -->
						<span v-if="!isActive" :class="[collapseIconClosed, headerIconColor]" aria-label="Card geschlossen" />
						<!-- Opened Icon -->
						<span v-else :class="[collapseIconOpened, headerIconColor]" aria-label="Card geöffnet" />
					</transition>
				</div>
				<slot name="collapseRight" />
			</div>
		</component>

		<transition name="ui-card--collapse" @before-enter="emit('update:isOpen', true);" @enter="openCard" @after-enter="afterOpenCard" @before-leave="beforeCloseCard" @leave="closeCard" @after-leave="emit('update:isOpen', false);">
			<div v-show="isActive" :id="'cardBody' + instanceId" class="ui-card--body-wrapper" ref="bodyWrapperRef">
				<div class="ui-card--body" :class="borderColor">
					<!-- Content Section -->
					<div class="ui-card--body--content">
						<!-- Left Button Section -->
						<div v-if="showContentLeftButton" id="ui-card--button-content-left" />
						<slot name="buttonContentLeft" />
						<!-- Main Section -->
						<span v-if="showContentMain" class="ui-card--content--main">{{ content }}</span>
						<slot />
						<!-- Right Button Section -->
						<div v-if="showContentRightButton" id="ui-card--button-content-right" />
						<div v-else-if="slots.buttonContentRight" class="ml-auto">
							<slot name="buttonContentRight" />
						</div>
					</div>
					<!-- Divider Section -->
					<hr v-if="showFooterDivider">
					<slot name="footerDivider" v-else />
					<!-- Footer Section -->
					<div v-if="showFooter" class="ui-card--body--footer">
						<!-- Left Button Section -->
						<div v-if="showFooterLeftButton" id="ui-card--button-footer-left" />
						<slot name="buttonFooterLeft" />
						<!-- Footer Main Content -->
						<span v-if="showFooterMain" class="ui-card--footer--main"> {{ footer }} </span>
						<slot name="footer" />
						<!-- Right Button Section -->
						<div v-if="showFooterRightButton" id="ui-card--button-footer-right" />
						<div v-else-if="slots.buttonFooterRight" class="ml-auto">
							<slot name="buttonFooterRight" />
						</div>
					</div>
				</div>
			</div>
		</transition>
	</div>

	<!-- Buttons -->
	<Teleport v-if="buttonContainerId && showButtons" defer :to="buttonContainerId">
		<div class="ui-card--buttons" :class="{ 'flex-col': (buttonOrientation === 'vertical'), 'ml-auto': (buttonPosition === 'right') }">
			<template v-for="(button) in buttons" :key="button.label">
				<SvwsUiTooltip :disabled="tooltipDisabled(button)" :position="buttonOrientation === 'vertical' ? 'right' : 'top'" :indicator="false">
					<template #content>
						{{ `${button.label}${(button.disabled && (button.disabledReason !== undefined ) && button.disabledReason.length !== 0)
							? `: ${button.disabledReason}` : ''}` }}
					</template>
					<SvwsUiButton class="ui-card--button" :disabled="button.disabled" @click="button.click"
						:type="props.buttonMode === 'text' ? button.type : 'icon'" :size="((props.buttonMode === 'text') && compact) ? 'small' : 'normal'"
						:aria-label="`Button ${button.label}`">
						<span :class="props.buttonMode === 'icon' ? [button.icon, button.iconType] : ''">
							{{ props.buttonMode === 'text' ? button.label : '' }}
						</span>
					</SvwsUiButton>
				</SvwsUiTooltip>
			</template>
		</div>
	</Teleport>
</template>


<script lang="ts" setup>

	import { ref, onMounted, computed, useSlots, watch, useId, toRaw } from 'vue';
	import type { ButtonType } from '../../types';
	import { ValidatorFehlerart } from '../../../../core/src/asd/validate/ValidatorFehlerart';

	const props = withDefaults(defineProps<{
		compact?: boolean;
		/* Collapse Optionen */
		isOpen?: boolean;
		collapsible?: boolean;
		collapseIconPosition?: 'left' | 'right';
		collapseIconOpened?: string;
		collapseIconClosed?: string;
		/* Header Optionen */
		icon?: string;
		title?: string;
		subtitle?: string;
		info?: string;
		/* Body Optionen */
		content?: string;
		showDivider?: boolean;
		footer?: string;
		/* Button Optionen */
		buttonMode?: 'text' | 'icon';
		buttonContainer?: 'content' | 'footer';
		buttonPosition?: 'left' | 'right';
		buttonOrientation?: 'vertical' | 'horizontal';
		onEdit?: () => void;
		onSave?: () => void;
		onDelete?: () => void;
		onCancel?: () => void;
		editButtonDisabled?: boolean;
		saveButtonDisabled?: boolean;
		deleteButtonDisabled?: boolean;
		cancelButtonDisabled?: boolean;
		editButtonDisabledReason?: string;
		saveButtonDisabledReason?: string;
		deleteButtonDisabledReason?: string;
		cancelButtonDisabledReason?: string;
		/* Validatoren */
		fehler?: ValidatorFehlerart;
	}>(), {
		compact: false,
		/* Collapse Optionen */
		isOpen: undefined,
		collapsible: true,
		collapseIconPosition: 'right',
		collapseIconOpened: 'i-ri-arrow-up-s-line',
		collapseIconClosed: 'i-ri-arrow-down-s-line',
		/* Header Optionen */
		icon: undefined,
		title: undefined,
		subtitle: undefined,
		info: undefined,
		/* Body Optionen */
		content: undefined,
		showDivider: false,
		footer: undefined,
		/* Button Optionen */
		buttonMode: 'text',
		buttonContainer: 'content',
		buttonPosition: 'right',
		buttonOrientation: 'horizontal',
		onEdit: undefined,
		onSave: undefined,
		onDelete: undefined,
		onCancel: undefined,
		editButtonDisabled: false,
		saveButtonDisabled: false,
		deleteButtonDisabled: false,
		cancelButtonDisabled: false,
		editButtonDisabledReason: undefined,
		saveButtonDisabledReason: undefined,
		deleteButtonDisabledReason: undefined,
		cancelButtonDisabledReason: undefined,
		/* Validatoren */
		fehler: () => ValidatorFehlerart.UNGENUTZT,
	});

	defineSlots();
	const emit = defineEmits<{
		"update:isOpen": [isOpen: boolean];
	}>();

	const bodyWrapperRef = ref<HTMLElement>();

	/**
	 * Berechnungen, wann welches Element angezeigt wird.
	 */
	const slots = useSlots();
	const showCollapseIconLeft = computed(() => !slots.collapseLeft && props.collapsible && (props.collapseIconPosition === 'left'));
	const showCollapseIconRight = computed(() => !slots.collapseRight && props.collapsible && (props.collapseIconPosition === 'right'));
	const showIcon = computed(() => !slots.icon && (props.icon !== undefined) && (props.icon.length !== 0));
	const showTitleWrapper = computed(() => slots.title || slots.subtitle || ((props.title !== undefined && props.title.length !== 0))
		|| ((props.subtitle !== undefined) && (props.subtitle.length !== 0)));
	const showTitle = computed(() => !slots.title && (props.title !== undefined) && (props.title.length !== 0));
	const showSubtitle = computed(() => !slots.subtitle && (props.subtitle !== undefined) && (props.subtitle.length !== 0));
	const showInfo = computed(() => !slots.info && (props.info !== undefined) && (props.info.length !== 0));
	const showContentMain = computed(() => !slots.default && (props.content !== undefined) && (props.content.length !== 0));
	const showContentLeftButton = computed(() => !slots.buttonContentLeft && showButtons.value && (props.buttonPosition === 'left')
		&& (props.buttonContainer === 'content'));
	const showContentRightButton = computed(() => !slots.buttonContentRight && showButtons.value && (props.buttonPosition === 'right')
		&& (props.buttonContainer === 'content'));
	const showFooterDivider = computed(() => !slots.footerDivider && props.showDivider);
	const showFooter = computed(() => slots.footer || slots.buttonFooterLeft || slots.buttonFooterRight
		|| ((props.footer !== undefined) && (props.footer.length !== 0)) || ((props.buttonContainer === 'footer') && showButtons.value));
	const showFooterMain = computed(() => !slots.footer && (props.footer !== undefined) && (props.footer.length !== 0));
	const showFooterRightButton = computed(() => !slots.buttonFooterRight && showButtons.value && (props.buttonContainer === 'footer')
		&& (props.buttonPosition === 'right'));
	const showFooterLeftButton = computed(() => !slots.buttonFooterLeft && showButtons.value && (props.buttonContainer === 'footer')
		&& (props.buttonPosition === 'left'));
	const showButtons = computed (() => buttons.value.length !== 0);

	const isHovered = ref(false);

	// Berechnung der Farben
	const headerBackgroundColor = computed(() => {
		if (isHovered.value) {
			switch (toRaw(props.fehler)) {
				case ValidatorFehlerart.HINWEIS:
					return (isActive.value) ? 'bg-ui-warning-hover' : 'bg-ui';
				case ValidatorFehlerart.KANN:
					return (isActive.value) ? 'bg-ui-caution-hover' : 'bg-ui';
				case ValidatorFehlerart.MUSS:
					return (isActive.value) ? 'bg-ui-danger-hover' : 'bg-ui';
				default:
					return (isActive.value) ? 'bg-ui-brand-hover' : 'bg-ui';
			}
		} else {
			switch (toRaw(props.fehler)) {
				case ValidatorFehlerart.HINWEIS:
					return (isActive.value) ? 'bg-ui-warning' : 'bg-ui-warning-secondary';
				case ValidatorFehlerart.KANN:
					return (isActive.value) ? 'bg-ui-caution' : 'bg-ui-caution-secondary';
				case ValidatorFehlerart.MUSS:
					return (isActive.value) ? 'bg-ui-danger' : 'bg-ui-danger-secondary';
				default:
					return (isActive.value) ? 'bg-ui-brand' : 'bg-ui-brand-secondary';
			}
		}


	});

	const headerTextColor = computed(() => {
		switch (toRaw(props.fehler)) {
			case ValidatorFehlerart.HINWEIS:
				return (isActive.value) ? 'text-ui-onwarning' : 'text-ui';
			case ValidatorFehlerart.KANN:
				return (isActive.value) ? 'text-ui-oncaution' : 'text-ui';
			case ValidatorFehlerart.MUSS:
				return (isActive.value) ? 'text-ui-ondanger' : 'text-ui';
			default:
				return (isActive.value) ? 'text-ui-onbrand' : 'text-ui';
		}
	});

	const headerIconColor = computed(() => {
		switch (toRaw(props.fehler)) {
			case ValidatorFehlerart.HINWEIS:
				return (isActive.value) ? '!icon-ui-onwarning' : '!icon-ui';
			case ValidatorFehlerart.KANN:
				return (isActive.value) ? '!icon-ui-oncaution' : '!icon-ui';
			case ValidatorFehlerart.MUSS:
				return (isActive.value) ? '!icon-ui-ondanger' : '!icon-ui';
			default:
				return (isActive.value) ? '!icon-ui-onbrand' : '!icon-ui';
		}
	});

	const borderColor = computed(() => {
		switch (toRaw(props.fehler)) {
			case ValidatorFehlerart.HINWEIS:
				return 'border-ui-warning';
			case ValidatorFehlerart.KANN:
				return 'border-ui-caution';
			case ValidatorFehlerart.MUSS:
				return 'border-ui-danger';
			default:
				return 'border-ui-brand';
		}
	});

	/**
	 * Setzt die ID für den Teleport, abhängig von der gewünschten Position der Buttons.
	 * Diese wird definiert durch die props: buttonContainer und buttonPosition.
	 */
	const buttonContainerId = computed (() => {
		if(!showButtons.value)
			return undefined;
		if(props.buttonContainer === "content")
			return (props.buttonPosition === "left") ? "#ui-card--button-content-left" : "#ui-card--button-content-right";
		else
			return (props.buttonPosition === "left") ? "#ui-card--button-footer-left" : "#ui-card--button-footer-right";
	});

	const ariaExpanded = computed(() => props.collapsible ? isActive.value : undefined);

	/**
	 * Berechnung der Buttons, die zur Verfügung gestellt werden. Verhindert, dass die Buttons im <template>-Bereich mehrfach definiert werden müssen.
	 */
	interface ButtonConfig {
		type: ButtonType;
		label: string;
		icon: string;
		iconType: string;
		disabled: boolean;
		disabledReason: string | undefined;
		click: () => void;
	}

	const buttons = computed<ButtonConfig[]>(() => {
		const buttons = new Array<ButtonConfig>();
		if (props.onSave !== undefined)
			buttons.push({
				type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-ui-brand', disabled: props.saveButtonDisabled,
				disabledReason: props.saveButtonDisabledReason, click: props.onSave,
			});
		if (props.onCancel !== undefined)
			buttons.push({
				type: 'secondary', label: 'Abbrechen', icon: 'i-ri-close-line', iconType: 'icon-ui-brand', disabled: props.cancelButtonDisabled,
				disabledReason: props.cancelButtonDisabledReason, click: props.onCancel,
			});
		if (props.onEdit !== undefined)
			buttons.push({
				type: 'primary', label: 'Bearbeiten', icon: 'i-ri-edit-2-line', iconType: 'icon-ui-brand', disabled: props.editButtonDisabled,
				disabledReason: props.editButtonDisabledReason, click: props.onEdit,
			});
		if (props.onDelete !== undefined)
			buttons.push({
				type: 'danger', label: 'Löschen', icon: 'i-ri-delete-bin-line', iconType: 'icon-ui-danger', disabled: props.deleteButtonDisabled,
				disabledReason: props.deleteButtonDisabledReason, click: props.onDelete,
			});
		return buttons;
	});

	// Wenn true, dann ist das Collapsible geöffnet
	const isActive = ref(false);
	// Wird für die eindeutige ID der Card benötigt, um diese für ARIA-Attribute zu verwenden
	const instanceId = useId();

	/**
	 * Wenn der Zustand isActive von außen manipuliert wird, wird dieser entsprechend gesetzt
	 */
	watch(() => props.isOpen, (newValue) => {
		setActive(newValue);
	});

	onMounted(() => {
		if (props.isOpen !== undefined) {
			setActive(props.isOpen);
		} else {
			setActive(!props.collapsible);
		}

		// Setzt die initiale Größe des collapsible Wrappers, um Transitions korrekt ausführen zu können.
		if (bodyWrapperRef.value !== undefined)
			bodyWrapperRef.value.style.maxHeight = isActive.value ? 'fit-content' : '0px';
	});

	/**
	 * Zustandsübergang beim Öffnen der Card.
	 *
	 * @param content   Der Contentbereich, dessen Höhe animiert werden soll
	 */
	async function openCard (content: Element, done: () => void) {
		const element = content as HTMLElement;
		// Wenn die Card bereits geöffnet ist (zum Beispiel initial beim Mounting), dann darf die Funktion nicht ausgeführt werden
		if (element.style.maxHeight === 'fit-content')
			return;
		element.style.maxHeight = `${element.scrollHeight}px`;
		element.addEventListener('transitionend', done, { once: true });
	};

	/**
	 * Zustand der Card nach dem Öffnen.
	 *
	 * @param content   Der Contentbereich, dessen Zusatnd nach dem Öffnen gesetzt werden soll.
	 */
	function afterOpenCard (content: Element) {
		const element = content as HTMLElement;
		element.style.maxHeight = 'fit-content';
	};

	/**
	 * Zustand der Card vor dem Öffnen.
	 *
	 * @param content   Der Contentbereich, dessen Zusatnd nach dem Öffnen gesetzt werden soll.
	 */
	function beforeCloseCard (el: Element) {
		const element = el as HTMLElement;
		element.style.maxHeight = `${element.scrollHeight}px`;
	};

	/**
	 * Zustandsübergang beim Scließen der Card.
	 *
	 * @param content   Der Contentbereich, dessen Höhe animiert werden soll
	 */
	async function closeCard (el: Element, done: () => void) {
		const element = el as HTMLElement;
		element.style.maxHeight = '0px';
		element.addEventListener('transitionend', done, { once: true });
	};


	/**
	 * Setzt die Card auf geöffnet (isActive) oder geschlossen.
	 *
	 * @param setActive   Der neue Wert von isActive. Falls nicht gesetzt, wird der Wert nur getoggelt.
	 */
	function setActive(setActive?: boolean): void {
		if (setActive !== undefined)
			isActive.value = setActive;
		else
			isActive.value = !isActive.value;
	}

	/**
	 * Berechnet, ob ein Tooltip für den Button angezeigt werden soll. Tooltips werden immer dann angezeigt, wenn die Funktion des Buttons nicht direkt
	 * ersichtlich ist (Icon-Buttons) oder wenn ein Button disabled ist und der Grund dafür im Tooltip erwähnt wird.
	 *
	 * @param button Der Button, für den überprüft wird, ob er ein Tooltip benötigt.
	 */
	function tooltipDisabled (button: ButtonConfig) {
		if (props.buttonMode === 'icon' || (button.disabled && (button.disabledReason !== undefined)))
			return false;
		else
			return true;
	}

</script>
