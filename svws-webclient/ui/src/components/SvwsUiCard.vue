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

		<!-- Body Section -->
		<div :id="'cardBody' + instanceId" ref="bodyWrapperRef" class="svws-ui-card--body-wrapper" :class="{ 'svws-active': isActive }">
			<div ref="bodyRef" class="svws-ui-card--body">
				<!-- Content Section -->
				<div class="body--content">
					<!-- Left Button Section -->
					<template v-if="showContentLeftButton">
						<div v-if="buttonMode === 'icon'" class="card--buttons" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<template v-for="(button, index) in buttons" :key="index">
								<SvwsUiTooltip :position="(buttonOrientation === 'vertical') ? 'right' : 'top'">
									<template #content>{{ button.label }}</template>
									<SvwsUiButton class="card--button" @click="button.click" type="icon" aria-label="Button {{ button.label }}">
										<span :class="[ button.icon, button.iconType ]" />
									</SvwsUiButton>
								</SvwsUiTooltip>
							</template>
						</div>
						<div v-else class="card--buttons" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<SvwsUiButton v-for="(button, index) in buttons" :key="index" :type="button.type"
								:size="compact ? 'small' : 'normal'" class="card--button" @click="button.click">
								<span>{{ button.label }}</span>
							</SvwsUiButton>
						</div>
					</template>
					<slot name="buttonContentLeft" />
					<!-- Main Section -->
					<span v-if="showContentMain" class="content--main">{{ content }}</span>
					<slot name="content" />

					<!-- Right Button Section -->
					<template v-if="showContentRightButton">
						<div v-if="buttonMode === 'icon'" class="card--buttons ml-auto" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<template v-for="(button, index) in buttons" :key="index">
								<SvwsUiTooltip :position="(buttonOrientation === 'vertical') ? 'right' : 'top'">
									<template #content>{{ button.label }}</template>
									<SvwsUiButton class="card--button" @click="button.click" type="icon" aria-label="Button {{ button.label }}">
										<span :class="[ button.icon, button.iconType ]" />
									</SvwsUiButton>
								</SvwsUiTooltip>
							</template>
						</div>
						<div v-else class="card--buttons ml-auto" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<SvwsUiButton v-for="(button, index) in buttons" :key="index" :type="button.type"
								:size="compact ? 'small' : 'normal'" class="card--button" @click="button.click">
								<span>{{ button.label }}</span>
							</SvwsUiButton>
						</div>
					</template>
					<slot name="buttonContentRight" />
				</div>

				<!-- Divider Section -->
				<hr v-if="showFooterDivider">
				<slot name="footerDivider" v-else />

				<!-- Footer Section -->
				<div v-if="showFooter" class="body--footer">
					<!-- Left Button Section -->
					<template v-if="showFooterLeftButton">
						<div v-if="buttonMode === 'icon'" class="card--buttons" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<template v-for="(button, index) in buttons" :key="index">
								<SvwsUiTooltip :position="(buttonOrientation === 'vertical') ? 'right' : 'top'">
									<template #content>{{ button.label }}</template>
									<SvwsUiButton class="card--button" @click="button.click" type="icon" aria-label="Button {{ button.label }}">
										<span :class="[ button.icon, button.iconType ]" />
									</SvwsUiButton>
								</SvwsUiTooltip>
							</template>
						</div>
						<div v-else class="card--buttons" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<SvwsUiButton v-for="(button, index) in buttons" :key="index" :type="button.type"
								:size="compact ? 'small' : 'normal'" class="card--button" @click="button.click">
								<span>{{ button.label }}</span>
							</SvwsUiButton>
						</div>
					</template>
					<slot name="buttonFooterLeft" />
					<!-- Footer Main Content -->
					<span v-if="showFooterMain" class="footer--main"> {{ footer }} </span>
					<slot name="footer" />
					<!-- Right Button Section -->
					<template v-if="showFooterRightButton">
						<div v-if="buttonMode === 'icon'" class="card--buttons ml-auto" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<template v-for="(button, index) in buttons" :key="index">
								<SvwsUiTooltip :position="(buttonOrientation === 'vertical') ? 'right' : 'top'">
									<template #content>{{ button.label }}</template>
									<SvwsUiButton class="card--button" @click="button.click" type="icon" aria-label="Button {{ button.label }}">
										<span :class="[ button.icon, button.iconType ]" />
									</SvwsUiButton>
								</SvwsUiTooltip>
							</template>
						</div>
						<div v-else class="card--buttons ml-auto" :class="{ 'flex-col': (buttonOrientation === 'vertical') }">
							<SvwsUiButton v-for="(button, index) in buttons" :key="index" :type="button.type"
								:size="compact ? 'small' : 'normal'" class="card--button" @click="button.click">
								<span>{{ button.label }}</span>
							</SvwsUiButton>
						</div>
					</template>
					<slot name="buttonFooterRight" />
				</div>
			</div>
		</div>
	</div>
</template>


<script lang="ts" setup>

	import { ref, onMounted, onBeforeUnmount, computed, useSlots, getCurrentInstance, watch } from 'vue';
	import type { ButtonType } from '../types';

	const props = withDefaults(defineProps<{
		compact?: boolean;
		isOpen?: boolean;
		collapsible?: boolean;
		collapseIconPosition?: 'left' | 'right';
		collapseIconOpened?: string;
		collapseIconClosed?: string;
		icon?: string;
		title?: string;
		subtitle?: string;
		info?: string;
		content?: string;
		showDivider?: boolean;
		footer?: string;
		buttonMode?: 'text' | 'icon';
		buttonContainer?: 'content' | 'footer';
		buttonPosition?: 'left' | 'right';
		buttonOrientation?: 'vertical' | 'horizontal';
		onEdit?: () => void;
		onSave?: () => void;
		onDelete?: () => void;
		onCancel?: () => void;
	}>(), {
		compact: true,
		isOpen: undefined,
		collapsible: true,
		collapseIconPosition: 'right',
		collapseIconOpened: 'i-ri-arrow-up-s-line',
		collapseIconClosed: 'i-ri-arrow-down-s-line',
		icon: undefined,
		title: undefined,
		subtitle: undefined,
		info: undefined,
		content: undefined,
		showDivider: false,
		footer: undefined,
		buttonMode: 'text',
		buttonContainer: 'content',
		buttonPosition: 'right',
		buttonOrientation: 'horizontal',
		onEdit: undefined,
		onSave: undefined,
		onDelete: undefined,
		onCancel: undefined,
	});

	const emit = defineEmits(['close', 'open']);

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
	const showContentLeftButton = computed(() => !slots.buttonContentLeft && (buttons.value.length !== 0) && (props.buttonPosition === 'left')
		&& (props.buttonContainer === 'content'));
	const showContentRightButton = computed(() => !slots.buttonContentRight && (buttons.value.length !== 0) && (props.buttonPosition === 'right')
		&& (props.buttonContainer === 'content'));
	const showFooterDivider = computed(() => !slots.footerDivider && props.showDivider);
	const showFooter = computed(() => slots.footer || ((props.footer !== undefined) && (props.footer.length !== 0)) || (props.buttonContainer === 'footer'));
	const showFooterMain = computed(() => !slots.footer && (props.footer !== undefined) && (props.footer.length !== 0));
	const showFooterRightButton = computed(() => !slots.buttonFooterRight && (props.buttonContainer === 'footer') && (props.buttonPosition === 'right'));
	const showFooterLeftButton = computed(() => !slots.buttonFooterLeft && (props.buttonContainer === 'footer') && (props.buttonPosition === 'left'));

	const ariaExpanded = computed(() => props.collapsible ? isActive.value : undefined);

	/**
	 * Berechnung der Buttons, die zur Verfügung gestellt werden. Verhindert, dass die Buttons im <template>-Bereich mehrfach definiert werden müssen.
	 */
	interface ButtonConfig {
		type: ButtonType;
		label: string;
		icon: string;
		iconType: string;
		click: () => void;
	}

	const buttons = computed<ButtonConfig[]>(() => {
		const buttons = new Array<ButtonConfig>();
		if (props.onSave !== undefined)
			buttons.push({ type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', click: props.onSave });
		if (props.onCancel !== undefined)
			buttons.push({ type: 'secondary', label: 'Abbrechen', icon: 'i-ri-close-line', iconType: 'icon-primary', click: props.onCancel });
		if (props.onEdit !== undefined)
			buttons.push({ type: 'primary', label: 'Bearbeiten', icon: 'i-ri-edit-2-line', iconType: 'icon-primary', click: props.onEdit });
		if (props.onDelete !== undefined)
			buttons.push({ type: 'danger', label: 'Löschen', icon: 'i-ri-delete-bin-line', iconType: 'icon-error', click: props.onDelete });
		return buttons;
	});

	// Wenn true, dann ist das Collapsible geöffnet
	const isActive = ref(false);

	const instanceId = ref<string>('');
	const bodyWrapperRef = ref<HTMLElement | null>(null);
	const bodyRef = ref<HTMLElement | null>(null);

	/**
	 * Wird benötigt, um die Animation beim Öffnen des Collapsibles zu erzeugen.
	 */
	const resizeObserver = new ResizeObserver(() => {
		if (isActive.value && bodyWrapperRef.value)
			bodyWrapperRef.value.style.maxHeight = getContentHeightWithBorder() + 'px';
	});

	/**
	 * Wenn der Zustand isActive von außen manipuliert wird, wird dieser entsprechend gesetzt
	 */
	watch(() => props.isOpen, (newValue) => {
		setActive(newValue);
	});

	onMounted(() => {
		const instance = getCurrentInstance();
		if (instance)
			instanceId.value = instance.uid.toString();

		if (props.collapsible)
			setActive((props.isOpen === undefined) ? false : props.isOpen)
		else
			setActive(true);
		
		if (bodyRef.value)
			resizeObserver.observe(bodyRef.value);
	});

	onBeforeUnmount(() => {
		if (bodyRef.value)
			resizeObserver.unobserve(bodyRef.value);
	});

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
		if (bodyWrapperRef.value) {
			if (isActive.value)
				bodyWrapperRef.value.style.maxHeight = getContentHeightWithBorder() + 'px';
			else
				bodyWrapperRef.value.style.maxHeight = '0';
		}

		isActive.value ? emit('open') : emit('close');
	}

	/**
	 * Berechnet die Höhe des Content-Bereichs inklusive des Rahmens, um eine korrekte Animation beim Öffnen und Schließen zu bewirken.
	 */
	function getContentHeightWithBorder(): number {
		if (bodyRef.value) {
			const computedStyle = getComputedStyle(bodyRef.value);

			const borderTop = computedStyle.borderTopWidth;
			const borderBottom = computedStyle.borderBottomWidth;
			const parsedBorderTop = borderTop !== '' ? parseFloat(borderTop) : 0;
			const parsedBorderBottom = borderBottom !== '' ? parseFloat(borderBottom) : 0;
			const borderHeight = parsedBorderTop + parsedBorderBottom;

			return bodyRef.value.scrollHeight + borderHeight;
		}
		return 0;
	}

</script>


<style lang="postcss" scoped>

	.svws-ui-card {
		@apply rounded-lg m-2 min-w-fit gap-3;

		.svws-ui-card--header {
			@apply p-4 flex items-center gap-3 text-left w-full rounded-lg bg-ui-brand-weak border border-ui-brand transition-[border-radius]
				duration-75 delay-[600ms] ease-in-out;

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
				@apply rounded-b-none transition-[border-radius] duration-100 delay-0 ease-in-out bg-ui-brand border-ui-brand;
			}
		}

		.svws-ui-card--body-wrapper {
			@apply transition-[max-height] duration-500 delay-75 ease-in-out overflow-hidden;

			&:not(.svws-active) .svws-ui-card--body {
				@apply transition-[border-color] duration-500 border-transparent;
			}

			&.svws-active {
				@apply transition-[max-height] duration-500 ease-in-out;
			}

			.svws-ui-card--body {
				@apply rounded-b-lg p-4 bg-ui-neutral border border-ui-brand flex gap-3 flex-col;

				.body--content {
					@apply flex gap-3 justify-between items-start;

					.content--main {
						@apply text-justify;
					}
				}

				.body--footer {
					@apply flex gap-3 items-center;

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

			.svws-ui-card--body {
				@apply border-ui-brand-hover;
			}
		}
	}

</style>
