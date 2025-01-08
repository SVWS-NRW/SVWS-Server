<template>
	<div class="svws-ui-card" :class="{ 'compact' : compact }" tabindex="0">
		<!-- Header Section -->
		<component :is="collapsible ? 'button' : 'div'" :type="collapsible ? 'button' : 'text'" :class="{ 'svws-active': isActive }" class="svws-ui-card--header"
			@click="collapsible ? setActive() : null" :aria-expanded="ariaExpanded" :aria-controls="collapsible ? 'cardBody' + instanceId : undefined">
			<!-- Left Collapse Icon -->
			<div v-if="showCollapseIconLeft" class="header--collapse-icon">
				<transition name="icon" mode="out-in">
					<!-- Closed Icon -->
					<span v-if="!isActive" :class="collapseIconClosed" aria-label="Card geschlossen" />
					<!-- Opened Icon -->
					<span v-else :class="collapseIconOpened" aria-label="Card geöffnet" />
				</transition>
			</div>
			<slot name="collapseLeft" />

			<!-- Icon Section -->
			<div v-if="showIcon" class="header--icon"> <span :class="[icon]" /> </div>
			<slot name="icon" />

			<!-- Title and Subtitle Section -->
			<div v-if="showTitleWrapper" class="header--title-wrapper">
				<div v-if="showTitle" class="header--title"> {{ title }} </div>
				<slot name="title" />
				<div v-if="showSubtitle" class="header--subtitle"> {{ subtitle }} </div>
				<slot name="subtitle" />
			</div>

			<!-- Right Section -->
			<div class="header--right-section">
				<!-- Info Section -->
				<div v-if="showInfo" class="header--info"> {{ info }} </div>
				<slot name="info" />

				<!-- Right Collapse Icon -->
				<div v-if="showCollapseIconRight" class="header--collapse-icon">
					<transition name="icon" mode="out-in">
						<!-- Closed Icon -->
						<span v-if="!isActive" :class="collapseIconClosed" aria-label="Card geschlossen" />
						<!-- Opened Icon -->
						<span v-else :class="collapseIconOpened" aria-label="Card geöffnet" />
					</transition>
				</div>
				<slot name="collapseRight" />
			</div>
		</component>

		<transition name="collapse" @before-enter="emit('update:isOpen', true);" @enter="openCard" @after-enter="afterOpenCard" @before-leave="beforeCloseCard" @leave="closeCard" @after-leave="emit('update:isOpen', false);">
			<div v-show="isActive" :id="'cardBody' + instanceId" class="svws-ui-card--body-wrapper" ref="bodyWrapperRef">
				<div class="svws-ui-card--body">
					<!-- Content Section -->
					<div class="body--content">
						<!-- Left Button Section -->
						<div v-if="showContentLeftButton" id="svws-ui-card--button-content-left" />
						<slot name="buttonContentLeft" />
						<!-- Main Section -->
						<span v-if="showContentMain" class="content--main">{{ content }}</span>
						<slot name="content" />
						<!-- Right Button Section -->
						<div v-if="showContentRightButton" id="svws-ui-card--button-content-right" />
						<div v-else-if="slots.buttonContentRight" class="ml-auto">
							<slot name="buttonContentRight" />
						</div>
					</div>
					<!-- Divider Section -->
					<hr v-if="showFooterDivider">
					<slot name="footerDivider" v-else />
					<!-- Footer Section -->
					<div v-if="showFooter" class="body--footer">
						<!-- Left Button Section -->
						<div v-if="showFooterLeftButton" id="svws-ui-card--button-footer-left" />
						<slot name="buttonFooterLeft" />
						<!-- Footer Main Content -->
						<span v-if="showFooterMain" class="footer--main"> {{ footer }} </span>
						<slot name="footer" />
						<!-- Right Button Section -->
						<div v-if="showFooterRightButton" id="svws-ui-card--button-footer-right" />
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
		<div class="card--buttons" :class="{ 'flex-col': (buttonOrientation === 'vertical'), 'ml-auto': (buttonPosition === 'right') }">
			<template v-for="(button) in buttons" :key="button.label">
				<SvwsUiTooltip :disabled="tooltipDisabled(button)" :position="buttonOrientation === 'vertical' ? 'right' : 'top'" :indicator="false">
					<template #content>
						{{ `${button.label}${(button.disabled && (button.disabledReason !== undefined ) && button.disabledReason.length !== 0)
							? `: ${button.disabledReason}` : ''}` }}
					</template>
					<SvwsUiButton class="card--button" :disabled="button.disabled" @click="button.click"
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

	import { ref, onMounted, computed, useSlots, watch, nextTick, useId } from 'vue';
	import type { ButtonType } from '../../types';

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
	});

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
	const showContentMain = computed(() => !slots.content && (props.content !== undefined) && (props.content.length !== 0));
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

	/**
	 * Setzt die ID für den Teleport, abhängig von der gewünschten Position der Buttons.
	 * Diese wird definiert durch die props: buttonContainer und buttonPosition.
	 */
	const buttonContainerId = computed (() => {
		if(!showButtons.value)
			return undefined;
		if(props.buttonContainer === "content")
			return (props.buttonPosition === "left") ? "#svws-ui-card--button-content-left" : "#svws-ui-card--button-content-right";
		else
			return (props.buttonPosition === "left") ? "#svws-ui-card--button-footer-left" : "#svws-ui-card--button-footer-right";
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
				type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: props.saveButtonDisabled,
				disabledReason: props.saveButtonDisabledReason, click: props.onSave,
			});
		if (props.onCancel !== undefined)
			buttons.push({
				type: 'secondary', label: 'Abbrechen', icon: 'i-ri-close-line', iconType: 'icon-primary', disabled: props.cancelButtonDisabled,
				disabledReason: props.cancelButtonDisabledReason, click: props.onCancel,
			});
		if (props.onEdit !== undefined)
			buttons.push({
				type: 'primary', label: 'Bearbeiten', icon: 'i-ri-edit-2-line', iconType: 'icon-primary', disabled: props.editButtonDisabled,
				disabledReason: props.editButtonDisabledReason, click: props.onEdit,
			});
		if (props.onDelete !== undefined)
			buttons.push({
				type: 'danger', label: 'Löschen', icon: 'i-ri-delete-bin-line', iconType: 'icon-error', disabled: props.deleteButtonDisabled,
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


<style lang="postcss" scoped>

	.svws-ui-card {
		@apply rounded-lg m-2 min-w-fit gap-3;

		.svws-ui-card--header {
			@apply p-4 flex items-center gap-3 text-left w-full rounded-lg bg-ui-brand-weak border border-ui-brand transition-[border-radius]
				duration-100 delay-[450ms] ease-in-out;

			.header--icon span {
				@apply icon-xl block;
			}

			.header--title-wrapper {
				@apply flex flex-col justify-center;

				.header--title {
					@apply text-headline-md;
				}

				.header--subtitle {
					@apply text-sm;
				}
			}

			.header--right-section {
				@apply flex items-center ml-auto gap-3;

				.header--info {
					@apply font-medium text-headline-sm;
				}
			}

			.header--collapse-icon {
				.icon-enter-active,
				.icon-leave-active {
					@apply transition-opacity duration-200 ease-linear;
				}

				.icon-enter-from {
					@apply opacity-0;
				}

				.icon-enter-to {
					@apply opacity-100;
				}

				.icon-leave-active {
					@apply opacity-0;
				}

				span {
					@apply icon-lg block;
				}
			}

			&.svws-active {
				@apply rounded-b-none transition-[border-radius] delay-0 duration-100 ease-in-out bg-ui-brand border-ui-brand;
			}
		}

		.svws-ui-card--body-wrapper {
			@apply overflow-hidden rounded-b-lg;

			.svws-ui-card--body {
				@apply rounded-b-lg p-4 bg-ui-neutral flex gap-3 flex-col border border-ui-brand border-t-0;

				.body--content {
					@apply flex gap-3 justify-between items-start overflow-auto;
					scrollbar-width: thin;

					.content--main {
						@apply text-justify;
					}
				}

				.body--footer {
					@apply flex gap-3 items-center relative overflow-auto;
					scrollbar-width: thin;

					.footer--main {
						@apply text-justify text-ui-secondary;
					}
				}

				.card--buttons {
					@apply flex gap-3;

					.card--button {
						@apply flex justify-center items-center w-auto h-auto;

						&.button--icon {
							@apply !p-0 mt-1 box-border;

							span {
								@apply icon;
							}

							&:disabled {
								@apply icon-gray bg-transparent;
							}
						}

					}
				}
			}
		}

		&.compact {
			.svws-ui-card--header {
				@apply p-1 gap-1;

				.header--icon span,
				.header--collapse-icon span {
					@apply icon-sm;
				}

				.header--title-wrapper .header--title {
					@apply text-headline-sm;
				}

				.header--right-section {
					@apply gap-1;

					.header--info {
						@apply text-sm font-medium;
					}
				}
			}

			.svws-ui-card--body-wrapper .svws-ui-card--body {
				@apply p-2 gap-1;

				.body--content,
				.body--footer,
				.card--buttons {
					@apply gap-1;
				}
			}
		}

		&:hover,
		&:focus-visible,
		&:focus {
			.svws-ui-card--header {
				@apply bg-ui-brand-hover border-ui-brand-hover text-white;

				.header--icon span,
				.header--collapse-icon span {
					@apply icon-white;
				}
			}

			.svws-ui-card--body-wrapper .svws-ui-card--body {
				@apply border-ui-brand-hover;
			}
		}

		.collapse-enter-active,
		.collapse-leave-active {
			@apply transition-[max-height] duration-500 delay-[50ms] ease-in-out;
		}

		.collapse-leave-active {
			@apply delay-0;
		}
	}


</style>
