<template>
    <div>
        <div class="router-tab-bar--wrapper print:hidden">
            <div v-if="state.scrolled" class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-left">
                <button class="router-tab-bar--scroll-button" @click="scroll('left')">
                   <Icon> <i-ri-arrow-left-line /> </Icon>
                </button>
            </div>
            <div ref="contentEl" class="tab-bar">
                <button v-for="route in props.routes" @click="select(route)" :class="[isSelected(route) ? 'router-tab-bar--button-active' : '', false ? 'hidden' : 'router-tab-bar--button']">
                    {{ route.meta?.text }}
                </button>
            </div>
            <div v-if="!state.scrolledMax"
                class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-right">
                <button class="router-tab-bar--scroll-button" @click="scroll('right')">
                    <Icon> <i-ri-arrow-right-line /> </Icon>
                </button>
            </div>
        </div>
        <div class="router-tab-bar--panel">
            <slot />
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { computed, onMounted, onUnmounted, onUpdated, ref, WritableComputedRef } from 'vue';
    import { RouteRecordRaw } from "vue-router";

    const props = defineProps<{
        routes: RouteRecordRaw[]
        modelValue: RouteRecordRaw
    }>();

    const emit = defineEmits<{ (e: 'update:modelValue', value: RouteRecordRaw): void, }>();

    type ComponentData = {
        scrolled: boolean;
        scrolledMax: boolean;
        scrollFactor: number;
        maxScrollLeft: number;
    }

    const contentEl = ref();
    const selected: WritableComputedRef<RouteRecordRaw> = computed({
        get() : RouteRecordRaw {
            return props.modelValue;
        },
        set(value: RouteRecordRaw ) {
            emit('update:modelValue', value);
        }
    });

    const state = ref<ComponentData>({
        scrolled: false,
        scrolledMax: false,
        scrollFactor: 4,
        maxScrollLeft: 0,
    });

    onMounted(() => {
        state.value.maxScrollLeft = (contentEl.value?.scrollWidth ?? 0) - (contentEl.value?.clientWidth ?? 0);
        state.value.scrolledMax = (contentEl.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
        contentEl.value?.addEventListener("scroll", handleScroll);
        window.addEventListener("resize", handleScroll);
    })


    onUnmounted(() => {
        contentEl.value?.removeEventListener("scroll", handleScroll);
        window.removeEventListener("resize", handleScroll);
    });


    onUpdated(() => {
        handleScroll();
    });


    function handleScroll() {
        state.value.scrolled = (contentEl.value?.scrollLeft ?? 0) > 0;
        state.value.maxScrollLeft =
            (contentEl.value?.scrollWidth ?? 0) - (contentEl.value?.clientWidth ?? 0);
        state.value.scrolledMax = (contentEl.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
    }

    function scroll(direction: 'left' | 'right') {
        const dir = direction == "left" ? -1 : 1;
        contentEl.value?.scrollBy({
            top: 0,
            left: (dir * contentEl.value.scrollWidth) / state.value.scrollFactor,
            behavior: "smooth"
        });
    }

    function select(route: RouteRecordRaw) {
        selected.value = route;
    }

    function isSelected(route: RouteRecordRaw) : boolean {
        return (route.name === selected.value.name)
    }

</script>


<style>

    .router-tab-bar--panel {
        @apply mt-0 mb-8 flex-grow overflow-y-auto px-6;
    }

    .router-tab-bar--wrapper {
        @apply flex items-center;
        @apply overflow-hidden;
        @apply relative;
        @apply rounded-full;
        @apply w-full;
        @apply flex-shrink-0;
        @apply px-4 mb-6;
    }

    .router-tab-bar {
        @apply bg-light;
        @apply flex flex-row items-center;
        @apply overflow-x-scroll;
        @apply relative;
        @apply rounded-full;
        @apply space-x-2;
        @apply w-full;

        -ms-overflow-style: none;
        /* Remove Scrollbar in IE and Edge */
        scrollbar-width: none;
        /* Remove Scrollbar in Firefox */
    }

    .router-tab-bar::-webkit-scrollbar {
        display: none;
        /* Remove Scrollbar in Chromium basesd Browsers */
    }

    .router-tab-bar--scroll-button-background {
        @apply absolute z-20;
        @apply h-full;
        @apply pointer-events-none;
        @apply from-transparent via-light to-light;
    }

    .router-tab-bar--scroll-button-background-right {
        @apply bg-gradient-to-r;
        @apply pl-8;
        @apply right-0;
        @apply rounded-r-full;
    }

    .router-tab-bar--scroll-button-background-left {
        @apply bg-gradient-to-l;
        @apply left-0;
        @apply pr-8;
        @apply rounded-l-full;
    }

    .router-tab-bar--scroll-button {
        @apply h-full;
        @apply inline-flex items-center justify-center;
        @apply pointer-events-auto;
        @apply px-3.5;
        @apply rounded-full;
        @apply text-black;
    }

    .router-tab-bar--scroll-button:focus {
        @apply outline-none ring ring-inset ring-primary ring-opacity-75;
    }

    .router-tab-bar--button {
        @apply bg-light;
        @apply inline-flex items-center justify-center;
        @apply py-2.5 px-5;
        @apply rounded-full;
        @apply select-none;
        @apply text-button font-bold text-dark;
        @apply whitespace-nowrap;
    }

    .router-tab-bar--button:focus {
        @apply outline-none ring ring-inset ring-primary ring-opacity-75;
    }

    .router-tab-bar--button-active {
        @apply bg-dark;
        @apply text-light;
    }

    .router-tab-bar--button:disabled {
        @apply cursor-not-allowed;
        @apply text-disabled-dark;
    }

</style>
